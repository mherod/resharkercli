package resharker.cli

fun main(args: Array<String>) = nativeMain {
    ApplicationArgParser().apply {
        parse(args)
        close()
    }
}
