package resharker.jiracli

import io.ktor.http.*

interface JiraCredentials {
    fun HttpMessageBuilder.authenticateRequest()
}
