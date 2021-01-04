package resharker.cli

fun isValidUrl(url: String) = url.matches("https?://\\S+".toRegex())
