package resharker.cli

import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.coroutines.runBlocking

@ExperimentalCli
class CurrentSubcommand(
    private val resharker: ResharkerCli,
) : Subcommand(
    name = "current",
    actionDescription = "Work on the current branch"
) {
    init {
        subcommands(
            CurrentKeySubcommand(),
            OpenCurrentIssueBrowser()
        )
    }

    inner class OpenCurrentIssueBrowser : Subcommand(
        name = "jira",
        actionDescription = "Open current issue in Jira"
    ) {
        override fun execute() = runBlocking {
            resharker.openCurrentBranchIssue()
        }
    }

    inner class CurrentKeySubcommand : Subcommand(
        name = "key",
        actionDescription = "Print current key"
    ) {
        override fun execute() = resharker.outputCurrentBranchKey()
    }

    override fun execute() {}
}
