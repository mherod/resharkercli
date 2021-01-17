package resharker.cli

inline infix fun String.requireMatch(regexString: String) = require(matches(regexString.toRegex())) {
    "$this must satisfy $regexString"
}

fun String.extract(regex: String) = regex.toRegex().find(this)?.value

fun String.toInitialism() = split("\\s".toRegex())
    .filter { it.isNotBlank() }
    .mapNotNull { it.firstOrNull() }
    .joinToString("")
