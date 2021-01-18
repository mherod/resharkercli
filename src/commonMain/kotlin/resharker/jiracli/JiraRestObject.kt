package resharker.jiracli

interface JiraRestObject {
    val id: String

    val key: String

    val self: String
}
