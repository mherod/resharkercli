package resharker.cli

import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.coroutines.runBlocking

@ExperimentalCli
class ReleaseSubcommand(val resharker: ResharkerCli) : Subcommand(
    name = "release",
    actionDescription = ""
) {
    init {
        subcommands(
            ReleaseNotesSubcommand()
        )
    }

    override fun execute() = TODO("Not yet implemented")

    inner class ReleaseNotesSubcommand : Subcommand(
        name = "notes",
        actionDescription = ""
    ) {
        override fun execute() = runBlocking {
            resharker.outputReleaseNotes()
        }
    }
}
