class WhoAreYou : Command {
    override val name = "whoru"

    override fun execute(user:String, args: String): String? {
        return  "I'm your friendly music bot. For a list of commands type !help. " +
                "For my source code visit https://github.com/AlwaysBeCrafting/dacapobot"
    }
}
