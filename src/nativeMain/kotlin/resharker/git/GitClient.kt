package resharker.git

interface GitClient {
    fun getCurrentBranch(): String
    fun getLastTag(): String
    fun getLogDiff(since: String): String
    fun getToolVersion(): String
}
