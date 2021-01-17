package resharker.cli

import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.coroutines.runBlocking

@ExperimentalCli
class ProjectSubcommand(
    private val resharker: ResharkerCli,
) : Subcommand(
    name = "project",
    actionDescription = "Project info"
) {

    init {
        subcommands(
            ProjectListSubcommand()
        )
    }

    inner class ProjectListSubcommand : Subcommand(
        name = "list",
        actionDescription = "List projects"
    ) {
        override fun execute() = runBlocking {
            resharker.outputProjectList()
        }
    }

    override fun execute() = Unit
}
