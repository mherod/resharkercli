package resharker.git.model

inline class Commitish(override val ref: String) : ProvidesRef {

    val length: Int get() = ref.length

    override fun toString(): String {
        check(ref.isNotBlank())
        return ref
    }
}

val HEAD: Commitish = Commitish("HEAD")
