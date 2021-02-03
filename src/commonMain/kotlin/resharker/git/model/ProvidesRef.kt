package resharker.git.model

interface ProvidesRef {
    val ref: String
    operator fun rangeTo(until: ProvidesRef): RefRange = RefRange("${ref}..${until.ref}")
}

inline fun <reified T : ProvidesRef> String.toRef(): T = Commitish(this) as T

inline fun <reified T : ProvidesRef> Iterable<String>.toRefs(): List<T> = map { it.toRef() }

