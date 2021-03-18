package resharker.jiracli

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.withTimeout
import resharker.cli.extractHostName
import resharker.cli.issueKeyRegex
import resharker.cli.requireMatch

class JiraClientKtorImpl(
    private val httpClient: HttpClient = HttpClient {
        install(JsonFeature) {
            serializer = jsonSerializer()
        }

    },
    private val rootUrl: String,
    private val credentials: JiraCredentials,
) : JiraClient {

    private inline fun HttpRequestBuilder.jiraRestUrl(crossinline function: URLBuilder.() -> Unit) = url {
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

    override suspend fun currentSessionUser(): JiraUserSimple = httpClient.request {
        url {
            protocol = URLProtocol.HTTPS
            host = rootUrl.extractHostName()
            path("rest", "auth", "latest", "session")
            configRequest()
        }
    }

    override suspend fun myself(): JiraUser = httpClient.request {
        url("$rootUrl/rest/api/3/myself")
        configRequest()
    }

    override suspend fun listUsers(): ArrayList<JiraUser> = httpClient.request {
        jiraRestUrl {
            path("rest", "api", "3", "users", "search")
            parameters.append("startAt", "0")
            parameters.append("maxResults", "100")
        }
    }

    override suspend fun listIssues(projectKey: String): JiraRest3IssueSearch = httpClient.request {
        jiraRestUrl {
            path("rest", "api", "3", "search")
            parameters.append("jql", "project = $projectKey")
        }
    }

    override suspend fun getIssue(key: String): JiraRest2Issue {
        key requireMatch issueKeyRegex
        return withTimeout(10_000) {
            httpClient.request {
                jiraRestUrl {
                    path("rest", "api", "2", "issue", key)
                }
            }
        }
    }

    override suspend fun assignIssue(key: String, assignee: String?) {
        key requireMatch issueKeyRegex
        httpClient.put<Unit> {
            contentType(ContentType.Application.Json)
            body = JiraRestAccountId(accountId = assignee)
            jiraRestUrl {
                path("rest", "api", "3", "issue", key, "assignee")
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
