package resharker.jiracli

interface IJiraClient {
    suspend fun listProjects(): ArrayList<JiraProject.JiraProjectItem>

    suspend fun getProject(id: String): JiraProject.JiraProjectItem

    suspend fun listIssues(projectKey: String): JiraRest3IssueSearch

    suspend fun getIssue(key: String): JiraRest2Issue

    fun close()
}

suspend inline fun IJiraClient.getProjectKeys(): Set<String> {
    return listProjects().map { it.key.toUpperCase() }.toSet()
}
