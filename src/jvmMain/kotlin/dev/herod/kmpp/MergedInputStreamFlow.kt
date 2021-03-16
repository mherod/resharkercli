package dev.herod.kmpp

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.merge

@ExperimentalCoroutinesApi
fun Process.mergedInputStreamFlow(): Flow<String> {
    val inputStreamFlow = inputStream
        .bufferedReader()
        .lineSequence()
        .asFlow()
    val errorStreamFlow = errorStream
        .bufferedReader()
        .lineSequence()
        .asFlow()
    return merge(inputStreamFlow, errorStreamFlow)
}
