package dev.herod.kmpp.files

import kotlinx.coroutines.flow.*
import java.io.File

actual fun file(absolutePath: String): KFile = KFileJvm(absolutePath = absolutePath)

data class KFileJvm(override val absolutePath: String) : KFile {

    private val file: File by lazy {
        // lazy to avoid unnecessary construction
        File(absolutePath)
    }

    override fun getParent(): KFile = file(absolutePath = file.parentFile.absolutePath)

    override fun exists(): Boolean = file.exists()

    override fun isDirectory(): Boolean = file.isDirectory

    override fun isFile(): Boolean = file.isFile

    override fun listFiles(): Flow<KFile> = flow {
        emitAll(
            flow = file.listFiles()
                .orEmpty()
                .asFlow()
                .map { file(it.absolutePath) }
        )
    }

    override fun size(): Long = file.length()

    override fun readLines(): Flow<String> = flow {
        emitAll(
            flow = file.readLines().asFlow()
        )
    }
}
