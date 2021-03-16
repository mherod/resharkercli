package dev.herod.kmpp

import org.junit.Test
import kotlin.test.assertNotNull

class FindAndroidToolKtTest {

    @Test
    fun findAndroidTool_adb(): Unit = runBlocking {
        assertNotNull(
            actual = lookupTool("adb", "ANDROID_HOME"),
        )
    }

    @Test
    fun findAndroidTool_emulator(): Unit = runBlocking {
        assertNotNull(
            actual = lookupTool("emulator", "ANDROID_HOME"),
        )
    }
}
