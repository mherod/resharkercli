package resharker.jiracli

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class JiraProject {

    @Serializable
    data class JiraProjectItem(
        @SerialName("avatarUrls")
        val avatarUrls: AvatarUrls,
        @SerialName("entityId")
        val entityId: String? = null,
        @SerialName("expand")
        val expand: String,
        @SerialName("id")
        val id: String,
        @SerialName("isPrivate")
        val isPrivate: Boolean,
        @SerialName("key")
        val key: String,
        @SerialName("name")
        val name: String,
        @SerialName("projectTypeKey")
        val projectTypeKey: String,
        @SerialName("properties")
        val properties: HashMap<String, String> = hashMapOf(),
        @SerialName("self")
        val self: String,
        @SerialName("simplified")
        val simplified: Boolean,
        @SerialName("style")
        val style: String,
        @SerialName("uuid")
        val uuid: String? = null,
    ) {
        @Serializable
        data class AvatarUrls(
            @SerialName("16x16")
            val x16: String,
            @SerialName("24x24")
            val x24: String,
            @SerialName("32x32")
            val x32: String,
            @SerialName("48x48")
            val x48: String,
        )
    }
}
