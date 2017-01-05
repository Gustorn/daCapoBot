class Help(private val config: Config, private val commandRegistry : CommandRegistry) : Command() {
    override val name = "help"
    override val help = "Need more help? Have a kitty. https://goo.gl/nRVwDf"

    override fun executeInternal(user: String, args: CommandArgs): String? {
        if (args.parameter == null) {
            return "!whoru, !help, !veto, !request, !suggestions"
        }

        val command = extractCommand(args.parameter, config, strictMatching = false)?.first
                ?: return "No help information found for $args"
        return commandRegistry[command]?.help ?: "$args is not a valid command"
    }
}