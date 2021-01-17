package resharker.cli

import kotlinx.coroutines.CoroutineScope

expect inline fun runBlocking(noinline block: suspend CoroutineScope.() -> Unit)
