package dev.herod.kx

fun Regex.extractGroup(input: String): String? = find(input)?.groupValues?.lastOrNull()
