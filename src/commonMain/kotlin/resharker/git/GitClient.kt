@file:Suppress("unused")

package resharker.git

import dev.herod.kx.requireNotNull
import resharker.git.model.*

interface GitClient {

    fun version(): String

    fun getCurrentBranch(): ProvidesRef

    fun describe(
        commitish: ProvidesRef = HEAD,
        abbrev: Int = 0,
    ): String

    fun getLogDiff(
        since: ProvidesRef,
        until: ProvidesRef = HEAD,
    ): String

    fun checkout(
        name: ProvidesRef?,
        newBranch: Boolean = false,
        track: ProvidesRef? = null,
    ): Boolean

    fun fetch(all: Boolean = false)

    fun push(
        remote: RemoteName = defaultRemote(),
        branch: ProvidesRef = HEAD,
        specifyUpstream: Boolean,
    )

    fun listBranches(remote: Boolean = false): Set<ProvidesRef>

    fun log(range: RefRange): String

    fun listTags(): List<CommitTag>

    fun showRef(ref: ProvidesRef): List<ProvidesRef>

    fun remote(): Remote

    fun setBranchUpstream(
        remote: RemoteName = defaultRemote(),
        branch: ProvidesRef = getCurrentBranch(),
    ): Boolean

    interface Remote {
        fun list(): Set<RemoteName>
    }
}

fun GitClient.defaultRemote(): RemoteName = remote().list().let { remotes ->
    when {
        remotes.size == 1 -> remotes.single() // only one remote
        origin in remotes -> origin // classic
        else -> remotes.minByOrNull { it.name }.requireNotNull() // shortest name (or first, won't happen)
    }
}

fun GitClient.listRemotes(): Set<RemoteName> = remote().list()

