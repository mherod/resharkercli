package resharker.cli

import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand

@ExperimentalCli
class CheckoutSubcommand(
    private val resharker: ResharkerCli,
) : Subcommand(
    name = "checkout",
    actionDescription = "Switch between tickets"
) {
    private val nextIssueKey: String by argument(
        type = ArgType.String,
        description = "The key of the issue to checkout"
    )

    override fun execute() = runBlocking {
        resharker.checkoutBranch(nextIssueKey)
    }
}
