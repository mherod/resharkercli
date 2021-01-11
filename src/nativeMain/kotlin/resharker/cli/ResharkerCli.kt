package resharker.cli

import resharker.git.GitClient
import resharker.jiracli.JiraClient
import resharker.jiracli.JiraIssue

class ResharkerCli(
    private val gitClient: GitClient,
    private val jiraClient: JiraClient,
) {

    fun greeting() {
        val branch = gitClient.getCurrentBranch()
        val issueKey = branch.extract(issueKeyRegex)?.correctIssueKey()
        val enclose = branch.extract(enclosedKeyRegex)
        val guess = branch.extract(otherwiseRegex)
        val branchKey = (issueKey ?: enclose ?: guess ?: branch).trim { it.isLetterOrDigit().not() }
        println("Branch key: $branchKey")
    }

    fun help() {
        TODO("Not yet implemented")
    }

    suspend fun outputReleaseNotes() {

        val branch = gitClient.getCurrentBranch()
        val lastTag = gitClient.getLastTag(from = detectMainBranch())

        println("Changes since $lastTag on branch $branch")

        val tickets = extractIssueKeys(
            dirtyInput = gitClient.getLogDiff(since = lastTag),
            projectKeys = jiraClient.getProjectKeys()
        )

        @Suppress("ConvertCallChainIntoSequence")
        tickets.map { jiraClient.getIssue(it) }
            .distinctBy(JiraIssue::id)
            .distinctBy(JiraIssue::key)
            .map { issue -> "${issue.key} ${issue.fields.summary}" }
            .distinct()
            .sorted()
            .forEach(::println)
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
        projectKeys: List<String>,
    ) = issueKeyRegex.toRegex()
        .findAll(dirtyInput)
        .flatMap { it.groupValues }
        .map { it.correctIssueKey(projectKeys = projectKeys) }
        .map { key -> key.trim { !it.isLetterOrDigit() } }
        .toSet()

    private tailrec fun String.correctIssueKey(
        projectKeys: List<String> = emptyList(),
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

fun hasMainBranchName(): (String) -> Boolean = { branchInput ->
    branchInput.substringAfter('/') in arrayOf("main", "master")
}

suspend fun JiraClient.getProjectKeys(): List<String> {
    return listProjects().map { it.key.toUpperCase() }.distinct()
}
