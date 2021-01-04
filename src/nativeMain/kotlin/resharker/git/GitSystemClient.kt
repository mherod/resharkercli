package resharker.git

import resharker.cli.exec

class GitSystemClient : GitClient {
    override fun getCurrentBranch(): String {
        return exec("git rev-parse --abbrev-ref HEAD").trim()
    }

    override fun getLastTag(): String {
        return exec("git describe origin/master --tags --abbrev=0").trim()
    }

    override fun getLogDiff(since: String): String {
        return exec("git log $since..HEAD --pretty=oneline --abbrev-commit")
    }
}
