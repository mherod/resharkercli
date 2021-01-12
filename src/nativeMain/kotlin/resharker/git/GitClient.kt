package resharker.git

interface GitClient {
    fun getCurrentBranch(): String
    fun getLastTag(from: String, abbrev: Int = 0): String
    fun getLogDiff(since: String): String
    fun getToolVersion(): String
    fun listBranches(remote: Boolean = false): Set<String>
    fun listRemotes(): Set<String>
}
