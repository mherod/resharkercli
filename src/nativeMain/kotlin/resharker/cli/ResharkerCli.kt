package resharker.cli

import resharker.GitSystemClient
import resharker.jiracli.JiraClient
import resharker.jiracli.JiraIssue

class ResharkerCli(
    val gitClient: GitSystemClient,
    val jiraClient: JiraClient,
) {

    suspend fun outputReleaseNotes() {

        val lastTag = gitClient.getLastTag()
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

    fun close() {
        jiraClient.close()
    }
}
