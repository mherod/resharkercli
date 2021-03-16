package dev.herod.kx.flow

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

fun loop(): Flow<Long> = flow {
    currentCoroutineContext().run {
        while (isActive) emit(0L)
    }
}
