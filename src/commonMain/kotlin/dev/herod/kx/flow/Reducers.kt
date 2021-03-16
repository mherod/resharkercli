@file:Suppress("unused")

package dev.herod.kx.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count

suspend fun <T> Flow<T>.any(function: (T) -> Boolean): Boolean = count { function(it) } > 0
