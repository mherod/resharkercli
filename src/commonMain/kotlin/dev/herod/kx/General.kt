package dev.herod.kx

inline fun <reified T> T?.requireNotNull(): T = requireNotNull(this)