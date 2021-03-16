package dev.herod.kmpp

actual fun envVar(key: String): String? = System.getenv(key)?.takeUnless { it.isBlank() }
