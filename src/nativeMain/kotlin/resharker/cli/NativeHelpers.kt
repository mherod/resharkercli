package resharker.cli

import kotlinx.cinterop.toKString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import platform.posix.exit
import platform.posix.getenv

inline fun requireEnv(key: String): String =
    requireNotNull(getenv(key)) { "Environmental variable $key is required" }.toKString()

fun nativeMain(block: suspend CoroutineScope.() -> Unit): Unit = runBlocking {
    runCatching {
        block()
    }.onFailure { throwable ->
        println(throwable.message)
    }.onFailure {
        exit(1)
    }.onSuccess {
        exit(0)
    }
}
