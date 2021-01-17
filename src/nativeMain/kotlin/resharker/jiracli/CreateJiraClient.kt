package resharker.jiracli

import resharker.cli.isValidUrl
import resharker.cli.requireEnv

fun createJiraClient(): IJiraClient = object : IJiraClient {

    private val delegate: IJiraClient by lazy {
        JiraClient(
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

    override suspend fun listProjects(): ArrayList<JiraProject.JiraProjectItem> = delegate.listProjects()

    override suspend fun getProject(id: String): JiraProject.JiraProjectItem = delegate.getProject(id)

    override suspend fun getIssue(key: String): JiraIssue = delegate.getIssue(key)

    override fun close() = delegate.close()
}
