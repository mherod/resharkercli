package dev.herod.kx

fun String.splitOnSpacing(): List<String> = split("\\s+".toRegex()).filterNot { it.isBlank() }
