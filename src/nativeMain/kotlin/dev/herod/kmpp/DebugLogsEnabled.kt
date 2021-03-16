package dev.herod.kmpp

import kotlinx.cinterop.toKString
import platform.posix.getenv

val debugLogsByIdePropEnabled: Boolean by lazy(getenv("DEBUG")?.toKString()::toBoolean)
val debugLogsByEnvEnabled: Boolean by lazy(getenv("DEBUG")?.toKString()::toBoolean)
actual val debugLogsEnabled: Boolean by lazy { debugLogsByEnvEnabled || debugLogsByIdePropEnabled }
