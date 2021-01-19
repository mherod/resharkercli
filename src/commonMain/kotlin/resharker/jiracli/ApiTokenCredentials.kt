package resharker.jiracli

import io.ktor.http.*
import io.ktor.util.*

@OptIn(InternalAPI::class)
fun apiTokenCredentials(
    username: String,
    apiToken: String,
) = object : JiraCredentials {
    override fun HttpMessageBuilder.authenticateRequest() {
        val base64 = "$username:$apiToken".encodeBase64()
        headers["Authorization"] = "Basic $base64"
    }
}
