@file:Suppress("NOTHING_TO_INLINE")

package resharker.cli

import kotlinx.coroutines.CoroutineScope

actual inline fun runBlocking(noinline block: suspend CoroutineScope.() -> Unit) =
    kotlinx.coroutines.runBlocking(block = block)
