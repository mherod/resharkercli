package resharker.cli

//language=RegExp
const val issueKeyRegex = "(\\w{1,5}+-\\d{1,5},?)+"

//language=RegExp
const val enclosedKeyRegex = "\\[(.+)]"

//language=RegExp
const val otherwiseRegex = "[^\\S](\\S\\w+)"
