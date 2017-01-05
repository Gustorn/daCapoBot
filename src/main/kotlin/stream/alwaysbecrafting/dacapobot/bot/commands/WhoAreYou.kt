class WhoAreYou : Command() {
    override val name = "whoru"
    override val help = "No extra arguments"

    override fun executeInternal(user:String, args: CommandArgs): String? {
        return  "I'm your friendly music bot. For a list of commands type !help. " +
                "For my source code visit https://github.com/AlwaysBeCrafting/dacapobot"
    }
}
