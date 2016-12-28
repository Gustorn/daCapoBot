class CommandRegistry(private val config: Config, database: Database, player: Player) {
    private val commands = listOf(
        Help(),
        Request(database),
        Suggestions(),
        Test(),
        Veto(database, player),
        WhoAreYou()
    ).associateBy(Command::name)

    operator fun get(name: String) = commands[name].let {
        if (it == null || (!config.isDebugModeEnabled && it.isDebugOnly)) {
            null
        } else {
            it
        }
    }
}
