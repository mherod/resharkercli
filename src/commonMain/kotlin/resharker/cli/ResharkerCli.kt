package resharker.cli

import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import resharker.git.GitClient
import resharker.git.model.*
import resharker.jiracli.*

class ResharkerCli(
    private val git: GitClient,
    private val jira: IJiraClient,
) {

    private val newBranchRefMap = LinkedHashMap<String, Deferred<ProvidesRef>>(10)

    suspend fun checkoutBranch(issueKey: String) = coroutineScope {
        val currentBranch = git.getCurrentBranch()
        val currentBranchKey = parseKey(input = currentBranch)
        if (currentBranchKey == issueKey) {
            println("Already on '$currentBranch'")
            return@coroutineScope
        }
        val remotes = git.remote().list()
        val fetchJob = launch {
            if (remotes.isNotEmpty())
                git.fetch(all = true)
        }
        parseKey(issueKey).let { key: String ->
            key requireMatch issueKeyRegex
            fetchJob.join()
            val matchedLocalBranch = git.listBranches(remote = false)
                .singleOrNull { parseKey(it.ref) == key }
            val matchedRemoteBranch = git.listBranches(remote = true)
                .singleOrNull { parseKey(it.ref) == key }

            val existingFromLocal = matchedLocalBranch != null && matchedRemoteBranch == null
            val existingSkipTrack = matchedLocalBranch != null && matchedRemoteBranch != null
            val newFromLocalCreate = matchedLocalBranch == null && matchedRemoteBranch == null
            val newFromRemoteCheckout = matchedRemoteBranch != null && matchedLocalBranch == null

            val newBranchRef: Deferred<ProvidesRef> = newBranchRefMap.getOrPut(key) {
                async {
                    "feature/${key}_${makeSummaryForBranch(key)}".toRef()
                }
            }
            val checkout = git.checkout(
                name = when {
                    newFromLocalCreate -> newBranchRef.await()
                    else -> matchedLocalBranch
                },
                newBranch = newFromLocalCreate,
                track = when {
                    existingSkipTrack -> null
                    newFromRemoteCheckout -> {
                        // matched remote, no local, we're going to checkout so specify remote
                        remotes.single() + newBranchRef.await()
                    }
                    newFromLocalCreate || existingFromLocal -> {
                        // no remote to track, we'll make new / already made locally, not pushed yet
                        // we'll add the upstream next
                        null
                    }
                    matchedRemoteBranch != null -> {
                        // matched remote, has local
                        matchedRemoteBranch
                    }
                    else -> error("unexpected branch state")
                }
            )
            if (checkout) {
                // checked out successfully
                if (matchedRemoteBranch != null) {
                    // there's a remote already, link up
                    git.setBranchUpstream()
                }
                // push branch, link to new upstream if creating it on remote
                git.push(
                    branch = newBranchRef.await(),
                    specifyUpstream = matchedRemoteBranch == null
                )
            }
        }
    }

    private suspend fun makeSummaryForBranch(issueKey: String): String {
        issueKey requireMatch issueKeyRegex
        return jira.getIssue(issueKey)
            .fields
            .summary
            .sanitisedForBranchPart()
    }

    fun currentBranchKey(): String = parseKey(input = git.getCurrentBranch())

    suspend fun openCurrentBranchIssue() {
        openBrowserForIssue(
            issue = jira.getIssue(key = currentBranchKey())
        )
    }

    suspend fun outputProjectList() {
        jira.listProjects().forEach { projectItem ->
            val key = projectItem.key
            val name = projectItem.name.trim()
            println(when (key) {
                name -> key
                name.toInitialism() -> "$key ($name)"
                else -> "$key $name"
            })
        }
    }

    suspend fun outputReleaseNotes() {

        val currentBranch = git.getCurrentBranch()
        val currentBranchRef = git.showRef(currentBranch).first()
        val mainBranch = git.showRef(detectMainBranch()).first()
        val currentTag = git.describe(commitish = mainBranch)

        val prevTag = if (currentBranchRef == mainBranch) {
            git.listTags()
                .takeWhile { it.name != currentTag }
                .last()
                .name
                .also {
                    println("Changes since $it on $currentBranchRef ($currentTag)")
                }
        } else {
            currentTag.also {
                println("Changes since $it on branch $currentBranch")
            }
        }

        val issueKeys = extractIssueKeys(
            dirtyInput = git.getLogDiff(since = prevTag.toRef(), until = currentBranchRef),
            projectKeys = jira.getProjectKeys()
        )

        getJiraIssues(issueKeys = issueKeys)
            .map { it.summaryString() }
            .distinctUntilChanged()
            .collect { println(it) }
    }

    private suspend fun getJiraIssues(issueKeys: Set<String>): Flow<JiraRest2Issue> {
        return issueKeys.sorted()
            .asFlow()
            .distinctUntilChanged()
            .map { jira.getIssue(it) }
            .distinctUntilChanged()
    }

    private fun detectMainBranch(): ProvidesRef {
        return git.listBranches(remote = true)
            .filterIsInstance<Commitish>()
            .filter(hasMainBranchName())
            .minByOrNull(Commitish::length)
            ?: error("Couldn't determine main branch")
    }

    fun outputVersion() {
        println("Git version: ${git.version()}")
    }

    fun close() {
        jira.close()
    }

    fun parseKey(input: Any): String {
        val s = input.toString()
        require(s.isNotBlank())
        val issueKey = s.extract(issueKeyRegex)?.correctIssueKey()
        val enclose = s.extract(enclosedKeyRegex)
        val guess = s.extract(otherwiseRegex)
        return (issueKey ?: enclose ?: guess ?: s).trim { it.isLetterOrDigit1().not() }
    }

    private fun projectKeys() = runBlocking {
        withTimeoutOrNull(2_000) { jira.getProjectKeys() }.orEmpty().toSet()
    }

    private fun extractIssueKeys(
        dirtyInput: String,
        projectKeys: Set<String>,
    ) = issueKeyRegex
        .findAll(dirtyInput)
        .flatMap { it.groupValues }
        .distinct()
        .map { it.correctIssueKey(projectKeys = projectKeys) }
        .map { key -> key.trim { !it.isLetterOrDigit1() } }
        .distinct()
        .toSet()

    private tailrec fun String.correctIssueKey(
        projectKeys: Set<String> = projectKeys(),
    ): String {
        val project = substringBefore('-').toUpperCase()
        val issueNum = substringAfter('-').trim { !it.isDigit1() }
        return when {
            project in projectKeys -> "$project-$issueNum"
            projectKeys.isEmpty() -> "$project-$issueNum"
            else -> replace(
                oldValue = project,
                newValue = projectKeys.maxByOrNull { project.commonPrefixWith(it) } ?: project,
                ignoreCase = true
            ).correctIssueKey(projectKeys)
        }
    }

    suspend fun queryIssuesJql(jql: String): List<JiraRest3Issue> {
        return jira.searchIssues(jql).issues
    }

    suspend fun outputQueryResult(query: String) {
        jira.searchIssues(query).let { result ->
            val issues = result.issues
            when {
                issues.isEmpty() -> {
                    println("No results")
                }
                else -> issues.forEach { issue ->
                    println(issue.summaryString())
                }
            }
        }
    }

    private fun JiraRestIssue.summaryString(): String {
        return "\t$key ${fields.summary} (${fields.status.name})"
    }

}

fun openBrowserForIssue(issue: JiraRestIssue) {
    val urlRoot = issue.self
    val url = URLBuilder(urlRoot)
        .path("browse", issue.key)
        .build()
    println("Opening \"$url\"")
    exec("open \"$url\"")
}

fun ResharkerCli.outputCurrentBranchKey() = println(currentBranchKey())

fun hasMainBranchName(): (Commitish) -> Boolean = { branchInput ->
    branchInput.ref.substringAfter('/') in arrayOf(
        "main",
        "master"
    )
}

fun String.sanitisedForBranchPart(): String {
    return toLowerCase().replace("[\\\\/\\s]+".toRegex(), "-")
}
