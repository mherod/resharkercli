package resharker.cli

import org.junit.Test
import resharker.git.MockGitClient
import resharker.jiracli.MockJiraClient

class ResharkerCliTest {

    private val resharkerCli = ResharkerCli(
        git = MockGitClient(),
        jira = MockJiraClient()
    )

    @Test
    fun extractIssueKeysTest() {
        val issueKeys = resharkerCli.extractIssueKeys(
            dirtyInput = "APPS-9999 APPS-1000 APPS-2000",
            projectKeys = setOf("APPS")
        )
        check("APPS-1000" in issueKeys)
        check("APPS-2000" in issueKeys)
        check("APPS-9999" in issueKeys)
    }
}
