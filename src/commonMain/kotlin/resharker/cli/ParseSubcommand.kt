package resharker.cli

import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand

@ExperimentalCli
class ParseSubcommand(val resharker: ResharkerCli) : Subcommand(
    name = "parse",
    actionDescription = "Parse input strings"
) {
    init {
        subcommands(
            ParseIssueKeySubcommand()
        )
    }

    override fun execute() {
    }

    inner class ParseIssueKeySubcommand : Subcommand(
        name = "key",
        actionDescription = "Parse issue key for current branch"
    ) {
        private val input: String by argument(
            type = ArgType.String,
            fullName = "input"
        )

        override fun execute() = runBlocking {
            println(resharker.parseKey(input))
        }
    }
}
