package resharker.cli

import dev.herod.assertions.assertContains
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
        resharkerCli.extractIssueKeys(
            dirtyInput = "APPS-9999 APPS-1000 APPS-2000",
            projectKeys = setOf("APPS")
        ).assertContains(
            "APPS-1000",
            "APPS-2000",
            "APPS-9999"
        )
    }

    @Test
    fun extractIssueKeysTest2() {
        resharkerCli.extractIssueKeys(
            dirtyInput = "APPS-2041_APPS-2042_APPS-2043",
            projectKeys = setOf("APPS")
        ).assertContains(
            "APPS-2041",
            "APPS-2042",
            "APPS-2043"
        )
    }
}
