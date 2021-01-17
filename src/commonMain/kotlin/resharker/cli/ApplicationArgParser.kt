package resharker.cli

import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli
import resharker.git.GitSystemClient
import resharker.jiracli.createJiraClient

@OptIn(ExperimentalCli::class)
class ApplicationArgParser : ArgParser(programName = "resharkercli") {

    private val resharker = ResharkerCli(
        jiraClient = createJiraClient(),
        gitClient = GitSystemClient()
    )

    init {
        subcommands(
            VersionSubcommand(resharker),
            ProjectSubcommand(resharker),
            CurrentSubcommand(resharker),
            ReleaseSubcommand(resharker),
            ParseSubcommand(resharker),
            QuerySubcommand(resharker)
        )
    }

    fun close() {
        resharker.close()
    }
}
