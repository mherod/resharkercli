package dev.herod.kmpp.files

import kotlinx.coroutines.flow.Flow

interface KFile {
    fun getParent(): KFile
    fun exists(): Boolean
    fun isDirectory(): Boolean
    fun isFile(): Boolean
    fun listFiles(): Flow<KFile>
    val absolutePath: String
    fun size(): Long
    fun readLines(): Flow<String>
}

expect fun file(absolutePath: String) : KFile
