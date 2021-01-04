package resharker.cli

import resharker.git.GitSystemClient
import resharker.jiracli.createJiraClient

fun main(args: Array<String>) = nativeMain {
    val resharker = ResharkerCli(
        jiraClient = createJiraClient() ?: error("Couldn't init Jira client"),
        gitClient = GitSystemClient()
    )
    if ("version" in args) {
        resharker.outputVersion()
    }
    if ("release" in args) {
        if ("notes" in args) {
            resharker.outputReleaseNotes()
        }
    }
    resharker.close()
}
