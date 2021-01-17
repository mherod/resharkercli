@file:Suppress("NOTHING_TO_INLINE")

package resharker.cli

import kotlinx.coroutines.CoroutineScope
import kotlin.system.exitProcess

actual inline fun mainBlock(crossinline block: suspend CoroutineScope.() -> Unit) {
    kotlinx.coroutines.runBlocking {
        runCatching { block() }
            .onFailure { throwable ->
                println("fatal: ${throwable.message?.substringAfter("fatal:")?.trim()}")
            }
            .onFailure { exitProcess(1) }
            .onSuccess { exitProcess(0) }
    }
    Unit
}

actual fun exec(command: String): String {
    return Runtime.getRuntime()
        .exec(command)
        .inputStream
        .bufferedReader()
        .readText()
}

actual inline fun requireEnv(key: String): String = requireNotNull(System.getenv(key))
