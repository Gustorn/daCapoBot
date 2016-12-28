class Help : Command {
    override val name = "help"

    override fun execute(user: String, args: String): String? {
        return "!whoru, !help, !veto, !request, !suggestions"
    }
}