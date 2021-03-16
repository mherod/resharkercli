package dev.herod.kmpp

import kotlinx.coroutines.flow.Flow

expect fun exec(command: String): Flow<String>
