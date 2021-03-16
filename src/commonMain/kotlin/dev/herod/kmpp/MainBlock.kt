package dev.herod.kmpp

import kotlinx.coroutines.CoroutineScope

expect inline fun mainBlock(crossinline block: suspend CoroutineScope.() -> Unit)
