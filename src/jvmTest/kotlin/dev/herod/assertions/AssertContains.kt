package dev.herod.assertions

fun <T : Any> Collection<T>.assertContains(vararg expects: T): Collection<T> = apply {
    for (expected in expects) {
        check(expected in this) {
            "actual = ${this}, expected contains $expected"
        }
    }
}