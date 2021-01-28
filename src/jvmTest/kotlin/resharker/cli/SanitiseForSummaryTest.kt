package resharker.cli

import kotlin.test.Test
import kotlin.test.assertEquals

class SanitiseForSummaryTest {

    @Test
    fun checkSanitiseForSummary() {
        assertEquals(
            expected = "hello-world-is-a-first-task",
            actual = "Hello/World is a      First TASK".sanitisedForBranchPart()
        )
    }
}
