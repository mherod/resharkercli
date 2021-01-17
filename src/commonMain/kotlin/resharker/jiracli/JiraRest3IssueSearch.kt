package resharker.jiracli

import kotlinx.serialization.Serializable

@Serializable
data class JiraRest3IssueSearch(
    val expand: String? = null,
    val startAt: Int = 0,
    val maxResults: Int = 50,
    val total: Int = 0,
    val issues: List<JiraRest3Issue> = emptyList()
)
