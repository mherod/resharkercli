package resharker.cli

import io.ktor.http.*
import kotlinx.coroutines.flow.*
import resharker.git.GitClient
import resharker.jiracli.IJiraClient
import resharker.jiracli.JiraIssue
import resharker.jiracli.getProjectKeys

class ResharkerCli(
    private val gitClient: GitClient,
    private val jiraClient: IJiraClient,
) {

    fun outputParsedKey(input: String) = println(parseKey(input))

    fun currentBranchKey(): String = parseKey(input = gitClient.getCurrentBranch())

    suspend fun openCurrentBranchIssue() {
        val currentBranchKey = currentBranchKey()
        val urlRoot = jiraClient.getIssue(key = currentBranchKey).self
        val url = URLBuilder(urlRoot)
            .path("browse", currentBranchKey)
            .build()
        println("Opening \"$url\"")
        exec("open \"$url\"")
    }

    suspend fun outputProjectList() {
        jiraClient.listProjects().forEach {
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

        val branch = gitClient.getCurrentBranch()
        val lastTag = gitClient.getLastTag(from = detectMainBranch())

        println("Changes since $lastTag on branch $branch")

        val issueKeys = extractIssueKeys(
            dirtyInput = gitClient.getLogDiff(since = lastTag),
            projectKeys = jiraClient.getProjectKeys()
        )

        getJiraIssues(issueKeys = issueKeys)
            .map { issue ->
                "\t${issue.key} ${issue.fields.summary} (${issue.fields.status.name})"
            }.distinctUntilChanged()
            .collect { println(it) }
    }

    private suspend fun getJiraIssues(issueKeys: Set<String>): Flow<JiraIssue> {
        return issueKeys.sorted()
            .asFlow()
            .distinctUntilChanged()
            .map { jiraClient.getIssue(it) }
            .distinctUntilChanged()
    }

    private fun detectMainBranch(): String {
        return gitClient.listBranches(remote = true)
            .filter(hasMainBranchName())
            .minByOrNull(String::length)
            ?: error("Couldn't determine main branch")
    }

    fun outputVersion() {
        println("Git version: ${gitClient.getToolVersion()}")
    }

    fun close() {
        jiraClient.close()
    }

    private fun parseKey(input: String): String {
        require(input.isNotBlank())
        val issueKey = input.extract(issueKeyRegex)?.correctIssueKey()
        val enclose = input.extract(enclosedKeyRegex)
        val guess = input.extract(otherwiseRegex)
        return (issueKey ?: enclose ?: guess ?: input).trim { it.isLetterOrDigit().not() }
    }

    private fun extractIssueKeys(
        dirtyInput: String,
        projectKeys: Set<String>,
    ) = issueKeyRegex
        .findAll(dirtyInput)
        .flatMap { it.groupValues }
        .distinct()
        .map { it.correctIssueKey(projectKeys = projectKeys) }
        .map { key -> key.trim { !it.isLetterOrDigit() } }
        .distinct()
        .toSet()

    private tailrec fun String.correctIssueKey(
        projectKeys: Set<String> = emptySet(),
    ): String {
        val project = substringBefore('-').toUpperCase()
        val issueNum = substringAfter('-').trim { !it.isDigit() }
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
}

fun ResharkerCli.outputCurrentBranchKey() = println(currentBranchKey())

inline fun hasMainBranchName(): (String) -> Boolean = { branchInput ->
    branchInput.substringAfter('/') in arrayOf(
        "main",
        "master"
    )
}
