package resharker.git.model

data class CommitTag(
    override val name: String,
    override val ref: String,
) : ProvidesName, ProvidesRef
