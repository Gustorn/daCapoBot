class Suggestions : Command {
    override val name = "suggestions"

    override fun execute(user:String, args: String): String? {
        return "Feature requests are welcome, please submit to https://github.com/AlwaysBeCrafting/dacapobot/issues"
    }
}
