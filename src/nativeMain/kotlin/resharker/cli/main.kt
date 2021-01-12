package resharker.cli

import resharker.git.GitSystemClient
import resharker.jiracli.createJiraClient

fun main(args: Array<String>) = nativeMain {
    val argString: String by lazy {
        args.joinToString(" ").trim()
    }
    val resharker = ResharkerCli(
        jiraClient = createJiraClient() ?: error("Couldn't init Jira client"),
        gitClient = GitSystemClient()
    )
    when {
        args.isEmpty() -> resharker.greeting()
        "version" in args -> resharker.outputVersion()
        "parse" in args -> when {
            "key" in args -> {
                resharker.outputParsedKey(input = argString.substringAfter("key").trim())
            }
        }
        "current" in args -> when {
            "key" in args -> resharker.outputCurrentBranchKey()
        }
        "project" in args -> when {
            "list" in args -> resharker.outputProjectList()
        }
        "release" in args -> when {
            "notes" in args -> resharker.outputReleaseNotes()
        }
        else -> resharker.help()
    }
    resharker.close()
}
