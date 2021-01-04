package resharker.cli

inline infix fun String.requireMatch(regexString: String) = require(matches(regexString.toRegex()))
