package resharker.git

import dev.herod.kmpp.exec
import kotlinx.coroutines.flow.toList
import resharker.cli.execBlocking
import resharker.cli.runBlocking
import resharker.git.model.*

class GitSystemClient : GitClient {

    override fun version(): String = runBlocking {
        exec("git version")
            .toList()
            .joinToString("\n")
            .trim()
            .let { "\\S*\\d+\\S*".toRegex().find(it)?.value ?: it }
    }

    override fun checkout(
        name: ProvidesRef?,
        newBranch: Boolean,
        track: ProvidesRef?,
    ): Boolean {
        return runCatching {
            execBlocking(
                command = buildString {
                    append("git checkout")
                    if (newBranch) {
                        append(" -b")
                    }
                    name?.let {
                        append(" ${it.ref}")
                    }
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
        execBlocking(
            command = buildString {
                append("git fetch")
                if (all) {
                    append(" --all")
                }
            }
        )
    }

    override fun push(remote: RemoteName, branch: ProvidesRef, specifyUpstream: Boolean) {
        execBlocking("git push ${if (specifyUpstream) "-u " else " "}${remote.name} ${branch.ref}")
    }

    override fun getCurrentBranch(): ProvidesRef {
        return execBlocking("git rev-parse --abbrev-ref HEAD").trim()
            .also { check(it.isNotBlank()) }
            .also { check(!it.startsWith("fatal:")) }
            .toRef()
    }

    override fun listBranches(remote: Boolean): Set<ProvidesRef> {
        return execBlocking("git branch${if (remote) " -r" else ""}")
            .split("[\\n|\\s]".toRegex())
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .toRefs<ProvidesRef>()
            .toSet()
    }

    override fun describe(commitish: ProvidesRef, abbrev: Int): String {
        return execBlocking("git describe ${commitish.ref} --tags --abbrev=$abbrev").trim()
            .also { check(it.isNotBlank()) }
            .also { check(!it.startsWith("fatal:")) }
    }

    override fun setBranchUpstream(remote: RemoteName, branch: ProvidesRef): Boolean {
        require(branch == getCurrentBranch()) // just a sanity check for now
        execBlocking("git branch --set-upstream-to ${remote.name}/${branch.ref}")
        return true
    }

    override fun log(range: RefRange): String = execBlocking(
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

    override fun listTags(): List<CommitTag> {
        return execBlocking("git tag")
            .split("[\\n|\\s]".toRegex())
            .map { each ->
                CommitTag(
                    name = each,
                    ref = each
                )
            }
    }

    override fun showRef(ref: ProvidesRef): List<ProvidesRef> {
        return execBlocking("git show-ref ${ref.ref}")
            .split("\\n".toRegex())
            .mapNotNull { s ->
                s.split("\\s".toRegex()).takeIf { it.size == 2 }?.let { hash ->
                    Commitish(hash.first())
                }
            }.distinct()
    }

    override fun getLogDiff(
        since: ProvidesRef,
        until: ProvidesRef,
    ): String = log(range = since..until)

    override fun remote(): GitClient.Remote = GitSystemRemote()

    inner class GitSystemRemote : GitClient.Remote {
        override fun list(): Set<RemoteName> = execBlocking("git remote")
            .split("[\\n|\\s]".toRegex())
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .map(::RemoteName)
            .toSet()
    }
}
