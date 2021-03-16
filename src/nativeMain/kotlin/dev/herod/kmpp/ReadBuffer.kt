package dev.herod.kmpp

import kotlinx.cinterop.*
import platform.posix.FILE
import platform.posix.fgets

fun MemScope.readToBuffer(
    bufferSize: Int,
    filePointer: CPointer<FILE>?,
) = allocArray<ByteVar>(bufferSize).let { buffer ->
    fgets(buffer, bufferSize, filePointer)?.toKString()
}
