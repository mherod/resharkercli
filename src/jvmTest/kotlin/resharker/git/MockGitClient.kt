package resharker.git

import resharker.git.model.CommitTag
import resharker.git.model.ProvidesRef
import resharker.git.model.RefRange
import resharker.git.model.RemoteName

class MockGitClient : GitClient {
    override fun version(): String {
        TODO("Not yet implemented")
    }

    override fun getCurrentBranch(): ProvidesRef {
        TODO("Not yet implemented")
    }

    override fun describe(commitish: ProvidesRef, abbrev: Int): String {
        TODO("Not yet implemented")
    }

    override fun getLogDiff(since: ProvidesRef, until: ProvidesRef): String {
        TODO("Not yet implemented")
    }

    override fun checkout(name: ProvidesRef?, newBranch: Boolean, track: ProvidesRef?): Boolean {
        TODO("Not yet implemented")
    }

    override fun fetch(all: Boolean) {
        TODO("Not yet implemented")
    }

    override fun push(remote: RemoteName, branch: ProvidesRef, specifyUpstream: Boolean) {
        TODO("Not yet implemented")
    }

    override fun listBranches(remote: Boolean): Set<ProvidesRef> {
        TODO("Not yet implemented")
    }

    override fun log(range: RefRange): String {
        TODO("Not yet implemented")
    }

    override fun listTags(): List<CommitTag> {
        TODO("Not yet implemented")
    }

    override fun showRef(ref: ProvidesRef): List<ProvidesRef> {
        TODO("Not yet implemented")
    }

    override fun remote(): GitClient.Remote {
        TODO("Not yet implemented")
    }

    override fun setBranchUpstream(remote: RemoteName, branch: ProvidesRef): Boolean {
        TODO("Not yet implemented")
    }
}