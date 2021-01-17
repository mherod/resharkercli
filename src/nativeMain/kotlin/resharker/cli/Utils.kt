package resharker.cli

inline infix fun String.requireMatch(regex: Regex) = require(matches(regex)) {
    "$this must satisfy $regex"
}

fun String.extract(regex: Regex) = regex.find(this)?.value

fun String.toInitialism() = split("\\s".toRegex())
    .filter { it.isNotBlank() }
    .mapNotNull { it.firstOrNull() }
    .joinToString("")
