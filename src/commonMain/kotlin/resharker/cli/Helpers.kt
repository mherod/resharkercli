package resharker.cli

import kotlinx.coroutines.CoroutineScope

expect inline fun mainBlock(crossinline block: suspend CoroutineScope.() -> Unit)

expect fun execBlocking(command: String): String

expect inline fun requireEnv(key: String): String
