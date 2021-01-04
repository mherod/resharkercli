package resharker.cli

import resharker.git.GitClient
import resharker.jiracli.JiraClient
import resharker.jiracli.JiraIssue

class ResharkerCli(
    private val gitClient: GitClient,
    private val jiraClient: JiraClient,
) {

    suspend fun outputReleaseNotes() {

        val branch = gitClient.getCurrentBranch()
        val lastTag = gitClient.getLastTag()

        println("Changes since $lastTag on branch $branch")

        val log = gitClient.getLogDiff(since = lastTag)

        val tickets = issueKeyRegex.toRegex()
            .findAll(log)
            .flatMap { it.groupValues }
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
}
