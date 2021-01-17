package resharker.cli

import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand

@ExperimentalCli
class VersionSubcommand(
    private val resharker: ResharkerCli,
) : Subcommand(
    name = "version",
    actionDescription = "Print tool versions"
) {
    override fun execute() = resharker.outputVersion()
}
