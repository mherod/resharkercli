package resharker.git.model

import kotlin.jvm.JvmInline

@JvmInline
value class RemoteName(override val name: String) : ProvidesName {
    override fun toString(): String = name
}

@Suppress("unused")
val origin = RemoteName("origin")

operator fun RemoteName.plus(branch: ProvidesRef): ProvidesRef = Commitish("$name/${branch.ref}")
