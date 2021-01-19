package resharker.git

import resharker.cli.exec
import resharker.git.model.*

class GitSystemClient : GitClient {
    override fun getToolVersion(): String {
        return exec("git version").trim()
            .let { "\\S*\\d+\\S*".toRegex().find(it)?.value ?: it }
    }

    override fun checkout(name: String, newBranch: Boolean): Boolean {
        return runCatching {
            exec(
                command = StringBuilder("git checkout")
                    .apply {
                        if (newBranch) {
                            append(" -b")
                        }
                        append(" $name")
                    }.toString()
            ).let { output ->
                check(!output.startsWith("fatal:"))
            }
            true
        }.getOrDefault(false)
    }

    override fun push(remote: RemoteName, branch: String) {
        exec("git push ${remote.name} $branch")
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

    override fun listRemotes(): Set<RemoteName> = remote().list()

    inner class GitSystemRemote : GitClient.Remote {
        override fun list(): Set<RemoteName> = exec("git remote")
            .split("[\\n|\\s]".toRegex())
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .map(::RemoteName)
            .toSet()
    }

    override fun remote(): GitClient.Remote = GitSystemRemote()

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
