package resharker.cli

import io.ktor.utils.io.core.*
import io.ktor.utils.io.streams.*
import kotlinx.cinterop.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import platform.posix.*

val debugLogsEnabled: Boolean by lazy {
    getenv("DEBUG_RESHARKERCLI")?.toKString().toBoolean()
}

actual inline fun requireEnv(key: String): String {
    return requireNotNull(getenv(key)) { "Environmental variable $key is required" }.toKString()
}

actual inline fun mainBlock(crossinline block: suspend CoroutineScope.() -> Unit): Unit = runBlocking {
    runCatching { block() }
        .onFailure { throwable ->
            println("fatal: ${throwable.message?.substringAfter("fatal:")?.trim()}")
        }
        .onFailure { exit(1) }
        .onSuccess { exit(0) }
}

@OptIn(ExperimentalIoApi::class)
actual fun execBlocking(command: String): String {
    if (debugLogsEnabled) println(">> $command")
    return popen(command, "r")?.use { pointer ->
        readToBuffer(
            file = pointer,
            pclose = true
        )
    }.toString().also { out ->
        if (debugLogsEnabled) println("<< ${out.ifBlank { "<< (blank)" }}")
    }
}

@OptIn(ExperimentalIoApi::class)
fun readFile(path: String): String = fopen(path, "r")?.use { readToBuffer(it) }.toString()

fun readToBuffer(file: CPointer<FILE>, pclose: Boolean = false): StringBuilder {
    val returnBuffer = StringBuilder()
    memScoped {
        val readBufferLength = 128
        val buffer = allocArray<ByteVar>(readBufferLength)
        var line = fgets(buffer, readBufferLength, file)?.toKString()
        while (line != null) {
            returnBuffer.append(line)
            line = fgets(buffer, readBufferLength, file)?.toKString()
        }
    }
    if (pclose) pclose(file)
    return returnBuffer
}
