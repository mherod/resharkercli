package dev.herod.kmpp

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
actual fun exec(command: String): Flow<String> {
    return Runtime.getRuntime().exec(command).mergedInputStreamFlow()
}
