package resharker.jiracli

interface IJiraClient {
    suspend fun listProjects(): ArrayList<JiraProject.JiraProjectItem>

    suspend fun getProject(id: String): JiraProject.JiraProjectItem

    suspend fun getIssue(key: String): JiraIssue
    fun close()
}
