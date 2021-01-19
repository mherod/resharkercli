package resharker.git.model

inline class RemoteName(override val name: String) : ProvidesName

@Suppress("unused")
val origin = RemoteName("origin")
