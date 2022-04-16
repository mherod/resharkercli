package resharker.git.model

import kotlin.jvm.JvmInline

@JvmInline
value class Commitish(override val ref: String) : ProvidesRef {

    init {
        check(ref.isNotBlank())
    }

    val length: Int get() = ref.length

    override fun toString(): String = ref
}

val HEAD: Commitish = Commitish("HEAD")
