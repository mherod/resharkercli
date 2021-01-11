package resharker.git

import resharker.cli.exec

class GitSystemClient : GitClient {
    override fun getToolVersion(): String {
        return exec("git version").trim()
            .let { "\\S*\\d+\\S*".toRegex().find(it)?.value ?: it }
    }

    override fun getCurrentBranch(): String {
        return exec("git rev-parse --abbrev-ref HEAD").trim()
            .also { check(it.isNotBlank()) }
            .also { check(!it.startsWith("fatal:")) }
    }

    override fun listBranches(remote: Boolean): List<String> {
        return exec("git branch${if (remote) " -r" else ""}")
            .split("[\\n|\\s]".toRegex())
            .map { it.trim() }
    }

    override fun getLastTag(from: String, abbrev: Int): String {
        return exec("git describe $from --tags --abbrev=$abbrev").trim()
            .also { check(it.isNotBlank()) }
            .also { check(!it.startsWith("fatal:")) }
    }

    override fun getLogDiff(since: String): String {
        return exec("git log $since..HEAD --pretty=oneline --abbrev-commit")
            .also { check(it.isNotBlank()) }
            .also { check(!it.startsWith("fatal:")) }
    }
}
