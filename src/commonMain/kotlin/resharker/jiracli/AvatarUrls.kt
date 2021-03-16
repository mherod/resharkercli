package resharker.jiracli

import kotlinx.serialization.Serializable

@Serializable
data class AvatarUrls(
    val `16x16`: String,
    val `24x24`: String,
    val `32x32`: String,
    val `48x48`: String
)