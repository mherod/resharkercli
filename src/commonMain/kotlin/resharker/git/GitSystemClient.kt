package resharker.git

import resharker.cli.exec
import resharker.git.model.*

class GitSystemClient : GitClient {
    override fun version(): String {
        return exec("git version")
            .trim()
            .let { "\\S*\\d+\\S*".toRegex().find(it)?.value ?: it }
    }

    override fun checkout(name: ProvidesRef?, newBranch: Boolean, track: ProvidesRef?): Boolean {
        return runCatching {
            exec(
                command = buildString {
                    append("git checkout")
                    if (newBranch) {
                        append(" -b")
                    }
                    append(" $name")
                    track?.let {
                        append(" --track")
                        append(" ${it.ref}")
                    }
                }
            ).let { output ->
                check(!output.startsWith("fatal:"))
            }
            true
        }.getOrDefault(false)
    }

    override fun fetch(all: Boolean) {
        exec(
            command = buildString {
                append("git fetch")
                if (all) {
                    append(" --all")
                }
            }
        )
    }

    override fun push(remote: RemoteName, branch: ProvidesRef) {
        exec("git push ${remote.name} ${branch.ref}")
    }

    override fun getCurrentBranch(): ProvidesRef {
        return exec("git rev-parse --abbrev-ref HEAD").trim()
            .also { check(it.isNotBlank()) }
            .also { check(!it.startsWith("fatal:")) }
            .toRef()
    }

    override fun listBranches(remote: Boolean): Set<ProvidesRef> {
        return exec("git branch${if (remote) " -r" else ""}")
            .split("[\\n|\\s]".toRegex())
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .toRefs<ProvidesRef>()
            .toSet()
    }

    override fun describe(commitish: ProvidesRef, abbrev: Int): String {
        return exec("git describe ${commitish.ref} --tags --abbrev=$abbrev").trim()
            .also { check(it.isNotBlank()) }
            .also { check(!it.startsWith("fatal:")) }
    }

    override fun log(range: RefRange): String = exec(
        command = buildString {
            append("git log")
            append(" ${range.value}")
            append(" --pretty=oneline")
            append(" --abbrev-commit")
        }
    ).also { s ->
        check(s.isNotBlank())
        check(!s.startsWith("fatal:"))
    }

    override fun getLogDiff(
        since: ProvidesRef,
        until: ProvidesRef,
    ): String = log(range = since..until)

    override fun remote(): GitClient.Remote = GitSystemRemote()

    inner class GitSystemRemote : GitClient.Remote {
        override fun list(): Set<RemoteName> = exec("git remote")
            .split("[\\n|\\s]".toRegex())
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .map(::RemoteName)
            .toSet()
    }
}
