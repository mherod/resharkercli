package resharker.cli

import dev.herod.assertions.assertEquals
import org.junit.Test

class ResharkerCliKtTest {

    @Test
    fun sanitisedForBranchPartTest() {
        "[Crashlytics] Junk!!! yeah. yeah"
            .sanitisedForBranchPart()
            .assertEquals("crashlytics-junk-yeah-yeah")
    }
}
