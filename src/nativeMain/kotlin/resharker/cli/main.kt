package resharker.cli

import resharker.GitSystemClient
import resharker.jiracli.createJiraClient

fun main(args: Array<String>) = nativeMain {
    val resharker = ResharkerCli(
        jiraClient = createJiraClient() ?: error("Couldn't init Jira client"),
        gitClient = GitSystemClient()
    )
    if ("release" in args) {
        if ("notes" in args) {
            resharker.outputReleaseNotes()
        }
    }
    resharker.close()
}
