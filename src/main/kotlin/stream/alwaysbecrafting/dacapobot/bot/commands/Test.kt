class Test : Command() {
    override val name = "test"
    override val help = "Nothing to see here"
    override val permission = CommandPermission.ADMIN

    override fun executeInternal(user: String, args: String?): String? {
        return "Slippy's not such a screw up after all"
    }
}
