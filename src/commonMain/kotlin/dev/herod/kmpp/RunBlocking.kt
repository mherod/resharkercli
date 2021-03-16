package dev.herod.kmpp

import kotlinx.coroutines.CoroutineScope

expect inline fun <T : Any> runBlocking(noinline block: suspend CoroutineScope.() -> T): T
