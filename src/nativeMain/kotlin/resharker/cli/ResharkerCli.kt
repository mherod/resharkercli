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
        val issueKey = branch.extract(issueKeyRegex)
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
        val lastTag = gitClient.getLastTag()

        val projectKeys = jiraClient.listProjects().map { it.key.toUpperCase() }

        println("Changes since $lastTag on branch $branch")

        val log = gitClient.getLogDiff(since = lastTag)

        val tickets = issueKeyRegex.toRegex()
            .findAll(log)
            .flatMap { it.groupValues }
            .map { key ->
                autoCorrectProjectKey(
                    possibleKey = key,
                    projectKeys = projectKeys
                )
            }
            .map { key -> key.trim { !it.isLetterOrDigit() } }
            .toSet()

        @Suppress("ConvertCallChainIntoSequence")
        tickets.map { jiraClient.getIssue(it) }
            .distinctBy(JiraIssue::id)
            .distinctBy(JiraIssue::key)
            .map { issue -> "${issue.key} ${issue.fields.summary}" }
            .distinct()
            .sorted()
            .forEach(::println)
    }

    fun outputVersion() {
        println("Git version: ${gitClient.getToolVersion()}")
    }

    fun close() {
        jiraClient.close()
    }

    private fun autoCorrectProjectKey(
        possibleKey: String,
        projectKeys: List<String>,
    ): String = when (val project = possibleKey.substringBefore('-').toUpperCase()) {
        in projectKeys -> possibleKey
        else -> possibleKey.replace(
            oldValue = project,
            newValue = projectKeys.maxByOrNull { project.commonPrefixWith(it) } ?: project
        )
    }
}
