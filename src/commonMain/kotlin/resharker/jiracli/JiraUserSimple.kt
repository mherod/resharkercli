package resharker.jiracli

import kotlinx.serialization.Serializable

@Serializable
data class JiraUserSimple(
    val name: String,
    val self: String
)