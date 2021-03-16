package dev.herod.kmpp

import dev.herod.kmpp.files.findFile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.singleOrNull

@OptIn(ExperimentalCoroutinesApi::class)
fun lookupTool(tool: String, homeDir: String): String = runBlocking {
    val easy = exec("which $tool").singleOrNull().orEmpty()
    if (easy.startsWith("/") && easy.endsWith(tool)) {
        return@runBlocking easy
    }
    val searchPath = envVar(homeDir)
    if (searchPath.isNullOrBlank()) {
        error("couldn't find $tool. try setting $homeDir environmental variable")
    }
    return@runBlocking findFile(
        searchPath = searchPath,
        fileName = tool
    ).map { file ->
        file.absolutePath
    }.onEmpty {
        error("couldn't find $tool. Check it is installed in your $homeDir")
    }.reduce { accumulator, value ->
        when {
            value.length < accumulator.length -> value
            else -> accumulator
        }.trim()
    }
}
