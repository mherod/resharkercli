package resharker.git

import resharker.git.model.Commitish
import resharker.git.model.HEAD
import resharker.git.model.ProvidesRef
import resharker.git.model.RemoteName

interface GitClient {

    fun getCurrentBranch(): Commitish

    fun describe(
        commitish: Commitish = HEAD,
        abbrev: Int = 0,
    ): String

    fun getLogDiff(since: ProvidesRef): String

    fun getToolVersion(): String

    fun checkout(name: String, newBranch: Boolean = false): Boolean

    fun push(remote: RemoteName = remote().list().single(), branch: String)

    fun listBranches(remote: Boolean = false): Set<Commitish>

    @Deprecated(
        message = "Use remote().list()",
        replaceWith = ReplaceWith("remote().list()"),
        level = DeprecationLevel.HIDDEN
    )
    fun listRemotes(): Set<RemoteName>

    fun remote(): Remote

    interface Remote {
        fun list(): Set<RemoteName>
    }
}

