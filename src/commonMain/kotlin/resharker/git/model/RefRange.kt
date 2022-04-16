package resharker.git.model

import kotlin.jvm.JvmInline

@JvmInline
value class RefRange(val value: String) {
    override fun toString(): String = value
}
