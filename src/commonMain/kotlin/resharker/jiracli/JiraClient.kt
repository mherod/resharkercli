package resharker.jiracli

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import resharker.cli.issueKeyRegex
import resharker.cli.requireMatch

class JiraClient(
    private val httpClient: HttpClient = HttpClient {
        install(JsonFeature) {
            serializer = jsonSerializer()
        }
    },
    private val rootUrl: String,
    private val credentials: JiraCredentials,
) : IJiraClient {

    private fun HttpRequestBuilder.configRequest() {
        credentials.apply {
            authenticateRequest()
        }
    }

    override suspend fun listProjects(): ArrayList<JiraProject.JiraProjectItem> = httpClient.request {
        url("$rootUrl/rest/api/2/project")
        configRequest()
    }

    override suspend fun getProject(id: String): JiraProject.JiraProjectItem = httpClient.request {
        url("$rootUrl/rest/api/2/project/${id}")
        configRequest()
    }

    override suspend fun getIssue(key: String): JiraIssue {
        key requireMatch issueKeyRegex
        return httpClient.request {
            url("$rootUrl/rest/api/2/issue/${key}")
            configRequest()
        }
    }

    override fun close() {
        httpClient.close()
    }
}
