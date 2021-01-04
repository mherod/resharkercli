package resharker.cli

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import platform.posix.*

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

fun exec(command: String): String {
    val returnBuffer = StringBuilder()
    val file = popen(command, "r")
    try {
        memScoped {
            val readBufferLength = 128
            val buffer = allocArray<ByteVar>(readBufferLength)
            var line = fgets(buffer, readBufferLength, file)?.toKString()
            while (line != null) {
                returnBuffer.append(line)
                line = fgets(buffer, readBufferLength, file)?.toKString()
            }
        }
    } finally {
        pclose(file)
    }
    return returnBuffer.toString()
}
