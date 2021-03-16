package dev.herod.kx

infix fun String.attributeString(name: String): String =
    "$name=\"([^\"]*)\"".toRegex().find(this)?.groupValues?.lastOrNull().orEmpty()
