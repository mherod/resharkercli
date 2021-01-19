package resharker.git

import resharker.git.model.Commitish
import resharker.git.model.HEAD
import resharker.git.model.ProvidesRef

interface GitClient {
    fun getCurrentBranch(): Commitish

    fun describe(
        commitish: Commitish = HEAD,
        abbrev: Int = 0,
    ): String

    fun getLogDiff(since: ProvidesRef): String

    fun getToolVersion(): String

    fun listBranches(remote: Boolean = false): Set<Commitish>

    fun listRemotes(): Set<String>
}
