package dev.herod.kx

inline fun <reified A : Enum<A>> guessForInput(name: String): A {
    return name.splitOnSpacing()
        .asReversed()
        .mapNotNull { part ->
            enumValues<A>().singleOrNull { enumValue ->
                enumValue.name.equals(part, ignoreCase = true)
            }
        }.first()
}
