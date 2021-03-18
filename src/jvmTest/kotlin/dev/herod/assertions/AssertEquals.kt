package dev.herod.assertions

fun Any.assertEquals(expected: Any) {
    check(this == expected) {
        println("actual = ${this}, expected = $expected")
    }
}