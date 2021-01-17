package resharker.jiracli

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import resharker.cli.extractHostName
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

    private fun HttpRequestBuilder.jiraRestUrl(function: URLBuilder.() -> Unit) = url {
        protocol = URLProtocol.HTTPS
        host = rootUrl.extractHostName()
        function(this)
        configRequest()
    }

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

    override suspend fun listIssues(projectKey: String): JiraRest3IssueSearch = httpClient.request {
        jiraRestUrl {
            path("rest", "api", "3", "search")
            parameters.append("jql", "project = $projectKey")
        }
    }

    override suspend fun getIssue(key: String): JiraRest2Issue {
        key requireMatch issueKeyRegex
        return httpClient.request {
            jiraRestUrl {
                path("rest", "api", "2", "issue", key)
            }
        }
    }

    override suspend fun searchIssues(jql: String): JiraRest3IssueSearch = httpClient.request {
        jiraRestUrl {
            path("rest", "api", "3", "search")
            parameters.append("jql", jql)
        }
    }

    override fun close() {
        httpClient.close()
    }
}
