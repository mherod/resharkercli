package dev.herod.kmpp.files

import kotlinx.coroutines.flow.Flow

expect fun findFile(searchPath: String, fileName: String): Flow<KFile>
