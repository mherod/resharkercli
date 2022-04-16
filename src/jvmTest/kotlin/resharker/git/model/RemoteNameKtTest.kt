package resharker.git.model

import org.junit.Test

class RemoteNameKtTest {

    @Test
    fun getOrigin() {
    }

    @Test
    fun plus() {
        val actual = origin + HEAD
        check(actual.ref == "origin/HEAD")
    }
}