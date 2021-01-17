package resharker.cli

import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand

@ExperimentalCli
class QuerySubcommand(
    private val resharker: ResharkerCli,
) : Subcommand(
    name = "query",
    actionDescription = "Perform queries"
) {
    init {
        subcommands(
            JqlSubcommand(),
        )
    }

    inner class JqlSubcommand : Subcommand(
        name = "jql",
        actionDescription = "Jira query"
    ) {
        private val query: String by argument(
            type = ArgType.String,
            description = "A raw Jira Query Language string"
        )

        override fun execute() = runBlocking {
            resharker.outputQueryResult(query)
        }
    }

    override fun execute() {}
}
