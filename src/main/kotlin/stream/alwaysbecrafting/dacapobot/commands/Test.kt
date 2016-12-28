class Test : Command {
    override val name = "test"

    override fun execute(user: String, args: String): String? {
        return "Slippy's not such a screw up after all"
    }
}
