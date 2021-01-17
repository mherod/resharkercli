package resharker.cli

import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.coroutines.runBlocking

@ExperimentalCli
class ReleaseSubcommand(val resharker: ResharkerCli) : Subcommand(
    name = "release",
    actionDescription = "Operate on a new or existing release"
) {
    init {
        subcommands(
            ReleaseNotesSubcommand()
        )
    }

    override fun execute() {
    }

    inner class ReleaseNotesSubcommand : Subcommand(
        name = "notes",
        actionDescription = "Output release notes related to the current branch"
    ) {
        override fun execute() = runBlocking {
            resharker.outputReleaseNotes()
        }
    }
}
