package dev.herod.kx

inline fun <reified T> runOrNull(function: () -> T): T? = runCatching(function).getOrNull()
