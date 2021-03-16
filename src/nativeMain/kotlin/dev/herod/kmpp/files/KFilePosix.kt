package dev.herod.kmpp.files

import dev.herod.kmpp.exec
import dev.herod.kx.flow.loop
import kotlinx.cinterop.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import platform.posix.*

@OptIn(FlowPreview::class, ExperimentalUnsignedTypes::class)
actual fun file(absolutePath: String): KFile = KFilePosix(absolutePath = absolutePath)

@FlowPreview
@ExperimentalUnsignedTypes
data class KFilePosix(
    override val absolutePath: String,
) : KFile {

    override fun getParent(): KFile = when {
        isFile() -> file(absolutePath = absolutePath.substringBeforeLast("/"))
        isDirectory() -> file(absolutePath = absolutePath.substringBeforeLast("/"))
        else -> error("can't determine parent")
    }

    override fun exists(): Boolean = access(absolutePath, F_OK) != -1

    override fun isDirectory(): Boolean = mode() - S_IFDIR.toUInt() < 1000u

    override fun isFile(): Boolean = mode() - S_IFREG.toUInt() < 1000u

    override fun listFiles(): Flow<KFile> = when {
        isFile() -> flowOf(this)
        isDirectory() -> exec(command = "ls \"$absolutePath\"").map { file("$absolutePath/$it") }
        else -> emptyFlow()
    }

    override fun size(): Long = memScoped {
        alloc<stat>().let { stat ->
            stat(absolutePath, stat.ptr)
            stat.st_size
        }
    }

    override fun readLines(): Flow<String> = flow {
        fopen(absolutePath, "r")?.let { pointer ->
            memScoped {
                val readBufferLength = 1024
                val buffer = allocArray<ByteVar>(readBufferLength)
                emitAll(
                    flow = loop().map {
                        fgets(buffer, readBufferLength, pointer)?.toKString()
                    }.takeWhile { s ->
                        s != null
                    }.flatMapConcat {
                        it?.split("\n".toRegex())?.asFlow() ?: emptyFlow()
                    }
                )
            }
            fclose(pointer)
        }
    }

    private fun mode(): UShort = memScoped {
        alloc<stat>().let { stat ->
            stat(absolutePath, stat.ptr)
            stat.st_mode
        }
    }
}
