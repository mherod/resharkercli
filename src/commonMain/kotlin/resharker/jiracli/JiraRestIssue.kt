package resharker.jiracli

interface JiraRestIssue {

    val id: String

    val key: String

    val self: String

    val fields: JiraRestIssueFields
}
