package resharker.jiracli

import kotlinx.serialization.Serializable

@Serializable
data class JiraUser(
    val accountId: String,
    val accountType: String? = null,
    val active: Boolean,
    val avatarUrls: AvatarUrls,
    val displayName: String,
    val locale: String? = null,
    val self: String,
    val timeZone: String? = null
)