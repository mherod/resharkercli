package resharker.jiracli

interface JiraClient {

    suspend fun listProjects(): ArrayList<JiraProject.JiraProjectItem>

    suspend fun getProject(id: String): JiraProject.JiraProjectItem

    suspend fun listIssues(projectKey: String): JiraRest3IssueSearch

    suspend fun getIssue(key: String): JiraRest2Issue

    suspend fun searchIssues(jql: String): JiraRest3IssueSearch

    fun close()
}

suspend inline fun JiraClient.getProjectKeys(): Set<String> {
    return listProjects().map { it.key.toUpperCase() }.toSet()
}
