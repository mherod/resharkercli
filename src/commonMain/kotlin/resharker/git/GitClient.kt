@file:Suppress("unused")

package resharker.git

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
        remote: RemoteName = remote().list().single(),
        branch: ProvidesRef = HEAD,
    )

    fun listBranches(remote: Boolean = false): Set<ProvidesRef>

    fun log(range: RefRange): String

    fun remote(): Remote

    interface Remote {
        fun list(): Set<RemoteName>
    }
}

fun GitClient.listRemotes(): Set<RemoteName> = remote().list()

