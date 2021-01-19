package resharker.cli

import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli
import resharker.git.GitSystemClient
import resharker.jiracli.createJiraClient

@OptIn(ExperimentalCli::class)
class ApplicationArgParser : ArgParser(programName = "resharkercli") {

    private val resharker = ResharkerCli(
        jira = createJiraClient(),
        git = GitSystemClient()
    )

    init {
        resharker.apply {
            subcommands(
                let(::VersionSubcommand),
                let(::ProjectSubcommand),
                let(::CheckoutSubcommand),
                let(::CurrentSubcommand),
                let(::ReleaseSubcommand),
                let(::ParseSubcommand),
                let(::QuerySubcommand),
            )
        }
    }

    fun close() {
        resharker.close()
    }
}
