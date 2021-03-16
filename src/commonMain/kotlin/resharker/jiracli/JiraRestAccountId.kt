package resharker.jiracli

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JiraRestAccountId(
    @SerialName("accountId") val accountId: String? = null
)
