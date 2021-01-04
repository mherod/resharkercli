package resharker

import resharker.cli.exec

class GitSystemClient {
    fun getCurrentBranch(): String {
        return exec("git rev-parse --abbrev-ref HEAD").trim()
    }

    fun getLastTag(): String {
        return exec("git describe origin/master --tags --abbrev=0").trim()
    }

    fun getLogDiff(since: String): String {
        return exec("git log $since..HEAD --pretty=oneline --abbrev-commit")
    }
}
