package dev.herod.kmpp.files

import dev.herod.kmpp.exec
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@FlowPreview
actual fun findFile(searchPath: String, fileName: String): Flow<KFile> {
    return exec(
        command = "find \"$searchPath\" -type f -name \"$fileName\""
    ).filter { output ->
        output.endsWith(fileName)
    }.map { s ->
        file(s)
    }
}
