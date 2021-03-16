package dev.herod.kx.flow

import dev.herod.kmpp.runBlocking
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn

fun <T> Flow<T>.launchBlocking(): Job = runBlocking { launchIn(this) }
