package resharker.cli

//language=RegExp
val issueKeyRegex = "([A-Za-z0-9]{1,5}+-\\d{1,5},?)+".toRegex()

//language=RegExp
val enclosedKeyRegex = "\\[(.+)]".toRegex()

//language=RegExp
val otherwiseRegex = "[^\\S](\\S\\w+)".toRegex()
