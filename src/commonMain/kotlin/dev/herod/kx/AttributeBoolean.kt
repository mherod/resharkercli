package dev.herod.kx

infix fun String.attributeBoolean(name: String): Boolean = attributeString(name).toBoolean()
