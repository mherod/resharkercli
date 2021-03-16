package dev.herod.kx.flow

import kotlinx.coroutines.flow.*

inline fun <reified T : Comparable<T>> Flow<T>.distinct(): Flow<T> = flow {
    emitAll(flow = toList().distinct().asFlow())
}
