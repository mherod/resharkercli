package resharker.jiracli

interface JiraRestIssueFields {
    val summary: String
    val status: JiraRestIssueStatus
}
