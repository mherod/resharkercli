package dev.herod.kmpp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import platform.posix.exit

actual inline fun mainBlock(crossinline block: suspend CoroutineScope.() -> Unit): Unit = runBlocking {
    runCatching { block() }
        .onFailure { throwable ->
            println("fatal: ${throwable.message?.substringAfter("fatal:")?.trim()}")
            if (debugLogsEnabled) {
                throwable.printStackTrace()
            }
        }
        .onFailure { exit(1) }
        .onSuccess { exit(0) }
}
