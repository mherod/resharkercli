package dev.herod.kmpp

import kotlinx.cinterop.toKString
import platform.posix.getenv

actual fun envVar(key: String): String? = getenv(key)?.toKString()?.takeUnless { it.isBlank() }
