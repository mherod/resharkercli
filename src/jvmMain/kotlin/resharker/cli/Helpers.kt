@file:Suppress("NOTHING_TO_INLINE")

package resharker.cli

import dev.herod.kmpp.exec
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlin.system.exitProcess

actual inline fun mainBlock(crossinline block: suspend CoroutineScope.() -> Unit) {
    kotlinx.coroutines.runBlocking {
        runCatching { block() }
            .onFailure { throwable ->
                val message = throwable.message ?: throwable.cause?.message
                println("fatal: ${message?.substringAfter("fatal:")?.trim()}")
                throwable.printStackTrace()
            }
            .onFailure { exitProcess(1) }
            .onSuccess { exitProcess(0) }
    }
    Unit
}

@ExperimentalCoroutinesApi
actual fun execBlocking(command: String): String = runBlocking {
    exec(command).toList().joinToString("\n")
}

actual inline fun requireEnv(key: String): String = requireNotNull(System.getenv(key))
