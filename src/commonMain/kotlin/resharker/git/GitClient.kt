@file:Suppress("unused")

package resharker.git

import resharker.git.model.*

interface GitClient {

    fun getCurrentBranch(): ProvidesRef

    fun describe(
        commitish: Commitish = HEAD,
        abbrev: Int = 0,
    ): String

    fun getLogDiff(since: ProvidesRef, until: ProvidesRef = "HEAD"): String

    fun version(): String

    fun checkout(name: String, newBranch: Boolean = false): Boolean

    fun push(
        remote: RemoteName = remote().list().single(),
        branch: ProvidesRef = HEAD,
    )

    fun listBranches(remote: Boolean = false): Set<ProvidesRef>

    fun remote(): Remote
    fun log(range: RefRange): String

    interface Remote {
        fun list(): Set<RemoteName>
    }
}

fun GitClient.listRemotes(): Set<RemoteName> = remote().list()

