package resharker.cli

import resharker.jiracli.JiraClient
import resharker.jiracli.JiraIssue
import resharker.jiracli.createJiraClient

fun main(args: Array<String>) = nativeMain {
    val jira: JiraClient = createJiraClient() ?: error("Couldn't init Jira client")

    if (args.any { it == "issue" } && args.any { it == "summary" }) {
        @Suppress("ConvertCallChainIntoSequence")
        extractIssueKeys(args)
            .map { jira.getIssue(it) }
            .distinctBy(JiraIssue::id)
            .distinctBy(JiraIssue::key)
            .map { issue -> "${issue.key} ${issue.fields.summary}" }
            .distinct()
            .toList()
            .forEach(::println)
    }

    if (args.any { it == "projects" } && args.any { it == "dump" }) {
        println(jira.listProjects())
    }

    jira.close()
}

private fun extractIssueKeys(args: Array<String>): List<String> {
    return args.mapNotNull { arg -> issueKeyRegex.toRegex().matchEntire(arg)?.groupValues }.flatten().distinct()
}

