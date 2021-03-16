package dev.herod.kx.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.takeWhile

inline fun <reified T : Any> Flow<T?>.takeWhileNotNull(): Flow<T> = takeWhile { it != null }.filterNotNull()
