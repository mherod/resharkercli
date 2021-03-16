package resharker.jiracli

class MockJiraClient : JiraClient {
    override suspend fun listProjects(): ArrayList<JiraProject.JiraProjectItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getProject(id: String): JiraProject.JiraProjectItem {
        TODO("Not yet implemented")
    }

    override suspend fun currentSessionUser(): JiraUserSimple {
        TODO("Not yet implemented")
    }

    override suspend fun myself(): JiraUser {
        TODO("Not yet implemented")
    }

    override suspend fun listUsers(): ArrayList<JiraUser> {
        TODO("Not yet implemented")
    }

    override suspend fun listIssues(projectKey: String): JiraRest3IssueSearch {
        TODO("Not yet implemented")
    }

    override suspend fun getIssue(key: String): JiraRest2Issue {
        TODO("Not yet implemented")
    }

    override suspend fun assignIssue(key: String, assignee: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun searchIssues(jql: String): JiraRest3IssueSearch {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}