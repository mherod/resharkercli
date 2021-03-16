package dev.herod.kmpp.files

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import java.io.File

actual fun findFile(searchPath: String, fileName: String): Flow<KFile> {
    return File(searchPath)
        .walkTopDown()
        .filter { it.isFile && it.name == fileName }
        .map { file(it.absolutePath) }
        .asFlow()
}
