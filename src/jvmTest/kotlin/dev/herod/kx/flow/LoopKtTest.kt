package dev.herod.kx.flow

import dev.herod.kmpp.runBlocking
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.take
import org.junit.Test
import kotlin.test.assertEquals

class LoopKtTest {

    @Test(timeout = 10_000)
    fun loopTest() = runBlocking {
        assertEquals(
            expected = 99_999_999,
            actual = loop().take(99_999_999).count()
        )
    }
}
