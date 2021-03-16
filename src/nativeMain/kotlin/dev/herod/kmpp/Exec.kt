package dev.herod.kmpp

import dev.herod.kx.flow.loop
import dev.herod.kx.flow.takeWhileNotNull
import kotlinx.cinterop.memScoped
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import platform.posix.pclose
import platform.posix.popen

@FlowPreview
actual fun exec(command: String): Flow<String> = flow {
    val pointer = popen(command, "r")
    memScoped {
        emitAll(
            flow = loop()
                .onStart { if (debugLogsEnabled) println(">> $command") }
                .map { readToBuffer(bufferSize = 1024, filePointer = pointer) }
                .takeWhileNotNull()
                .flatMapConcat { it.split("\n".toRegex()).asFlow() }
                .onEach { if (debugLogsEnabled) println("<< $it") }
        )
    }
    pclose(pointer)
}
