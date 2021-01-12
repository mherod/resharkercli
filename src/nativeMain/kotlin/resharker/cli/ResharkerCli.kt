package resharker.cli

import kotlinx.coroutines.flow.*
import resharker.git.GitClient
import resharker.jiracli.JiraClient
import resharker.jiracli.JiraIssue

class ResharkerCli(
    private val gitClient: GitClient,
    private val jiraClient: JiraClient,
) {

    fun greeting() {
        println("Branch key: ${currentBranchKey()}")
    }

    fun outputCurrentBranchKey() {
        println(currentBranchKey())
    }

    private fun currentBranchKey(): String {
        val branch = gitClient.getCurrentBranch()
        val issueKey = branch.extract(issueKeyRegex)?.correctIssueKey()
        val enclose = branch.extract(enclosedKeyRegex)
        val guess = branch.extract(otherwiseRegex)
        return (issueKey ?: enclose ?: guess ?: branch).trim { it.isLetterOrDigit().not() }
    }

    fun help() {
        TODO("Not yet implemented")
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

    private fun extractIssueKeys(
        dirtyInput: String,
        projectKeys: Set<String>,
    ) = issueKeyRegex.toRegex()
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

inline fun hasMainBranchName(): (String) -> Boolean = { branchInput ->
    branchInput.substringAfter('/') in arrayOf(
        "main",
        "master"
    )
}

suspend inline fun JiraClient.getProjectKeys(): Set<String> {
    return listProjects().map { it.key.toUpperCase() }.toSet()
}
