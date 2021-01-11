package resharker.cli

inline infix fun String.requireMatch(regexString: String) = require(matches(regexString.toRegex()))

fun String.extract(regex: String) = regex.toRegex().find(this)?.value
