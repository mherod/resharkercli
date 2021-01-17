package resharker.cli

fun main(args: Array<String>) = mainBlock {
    ApplicationArgParser().apply {
        parse(args)
        close()
    }
}
