class Suggestions : Command() {
    override val name = "suggestions"
    override val help = "Don't know how to submit a !suggestion? Here's more info than " +
                        "you probably wanted: https://guides.github.com/features/issues/"

    override fun executeInternal(user:String, args: String?): String? {
        return "Feature requests are welcome, please submit to https://github.com/AlwaysBeCrafting/dacapobot/issues"
    }
}
