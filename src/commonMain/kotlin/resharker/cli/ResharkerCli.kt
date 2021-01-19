package resharker.cli

import io.ktor.http.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withTimeoutOrNull
import resharker.git.GitClient
import resharker.git.model.Commitish
import resharker.git.model.toRef
import resharker.jiracli.*

class ResharkerCli(
    private val git: GitClient,
    private val jira: IJiraClient,
) {

    suspend fun checkoutBranch(issueKey: String) {
        parseKey(issueKey).let { key ->
            val summary = jira.getIssue(key)
                .fields
                .summary
                .toLowerCase()
                .replace("\\s+".toRegex(), "-")
            val newBranchName = "feature/${key}_$summary"
            if (git.checkout(name = newBranchName, newBranch = true)) {
                git.push(branch = newBranchName)
            }
        }
    }

    fun currentBranchKey(): String = parseKey(input = git.getCurrentBranch())

    suspend fun openCurrentBranchIssue() {
        openBrowserForIssue(
            issue = jira.getIssue(key = currentBranchKey())
        )
    }

    suspend fun outputProjectList() {
        jira.listProjects().forEach {
            val key = it.key
            val name = it.name.trim()
            println(when (key) {
                name -> key
                name.toInitialism() -> "$key ($name)"
                else -> "$key $name"
            })
        }
    }

    suspend fun outputReleaseNotes() {

        val branch = git.getCurrentBranch()
        val lastTag = git.describe(commitish = detectMainBranch())

        println("Changes since $lastTag on branch $branch")

        val issueKeys = extractIssueKeys(
            dirtyInput = git.getLogDiff(since = lastTag.toRef()),
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

    private fun detectMainBranch(): Commitish {
        return git.listBranches(remote = true)
            .filter(hasMainBranchName())
            .minByOrNull(Commitish::length)
            ?: error("Couldn't determine main branch")
    }

    fun outputVersion() {
        println("Git version: ${git.getToolVersion()}")
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
