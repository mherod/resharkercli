package resharker.jiracli

import resharker.cli.isValidUrl
import resharker.cli.requireEnv

fun createJiraClient(): JiraClient? = runCatching {
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
}.onFailure { throwable ->
    println(throwable.message)
}.getOrNull()
