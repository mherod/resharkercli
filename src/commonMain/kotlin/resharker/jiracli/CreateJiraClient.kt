package resharker.jiracli

import resharker.cli.isValidUrl
import resharker.cli.requireEnv

fun createJiraClient(): JiraClient = JiraClientImpl()

class JiraClientImpl : JiraClient {

    private val jiraClientLazy: Lazy<JiraClientKtorImpl> = lazy {
        JiraClientKtorImpl(
            rootUrl = requireEnv("JIRA_ROOT").also { rootUrl ->
                require(isValidUrl(rootUrl)) {
                    "JIRA_ROOT should be a valid URL (https://myjira.atlassian.com)"
                }
            },
            credentials = apiTokenCredentials(
                username = requireEnv("JIRA_USER"),
                apiToken = requireEnv("JIRA_TOKEN")
            )
        )
    }

    private val delegate: JiraClient by jiraClientLazy

    override suspend fun listProjects(): ArrayList<JiraProject.JiraProjectItem> = delegate.listProjects()

    override suspend fun getProject(id: String): JiraProject.JiraProjectItem = delegate.getProject(id)

    override suspend fun listIssues(projectKey: String): JiraRest3IssueSearch = delegate.listIssues(projectKey)

    override suspend fun getIssue(key: String): JiraRest2Issue = delegate.getIssue(key)

    override suspend fun searchIssues(jql: String): JiraRest3IssueSearch = delegate.searchIssues(jql)

    override fun close() {
        if (jiraClientLazy.isInitialized()) {
            delegate.close()
        }
    }
}
