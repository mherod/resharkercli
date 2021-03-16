package dev.herod.kx.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toSet

inline fun <reified T : Comparable<T>> Flow<T>.sorted(): Flow<T> = flow {
    toSet().distinct().sortedBy { it }.forEach { emit(it) }
}
