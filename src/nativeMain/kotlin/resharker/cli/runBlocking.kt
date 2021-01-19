package resharker.cli

import kotlinx.coroutines.CoroutineScope

actual inline fun <T : Any> runBlocking(noinline block: suspend CoroutineScope.() -> T): T {
    return kotlinx.coroutines.runBlocking(block = block)
}
