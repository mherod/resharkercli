package resharker.git

import resharker.cli.exec
import resharker.git.model.Commitish
import resharker.git.model.ProvidesRef
import resharker.git.model.toRef
import resharker.git.model.toRefs

class GitSystemClient : GitClient {
    override fun getToolVersion(): String {
        return exec("git version").trim()
            .let { "\\S*\\d+\\S*".toRegex().find(it)?.value ?: it }
    }

    override fun getCurrentBranch(): Commitish {
        return exec("git rev-parse --abbrev-ref HEAD").trim()
            .also { check(it.isNotBlank()) }
            .also { check(!it.startsWith("fatal:")) }
            .toRef()
    }

    override fun listBranches(remote: Boolean): Set<Commitish> {
        return exec("git branch${if (remote) " -r" else ""}")
            .split("[\\n|\\s]".toRegex())
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .toRefs<Commitish>()
            .toSet()
    }

    override fun listRemotes(): Set<String> {
        return exec("git remote").split("[\\n|\\s]".toRegex())
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .toSet()
    }

    override fun describe(commitish: Commitish, abbrev: Int): String {
        return exec("git describe $commitish --tags --abbrev=$abbrev").trim()
            .also { check(it.isNotBlank()) }
            .also { check(!it.startsWith("fatal:")) }
    }

    override fun getLogDiff(since: ProvidesRef): String {
        return exec("git log $since..HEAD --pretty=oneline --abbrev-commit")
            .also { check(it.isNotBlank()) }
            .also { check(!it.startsWith("fatal:")) }
    }
}
