class CommandRegistry(config: Config, database: Database, player: Player, bot: DaCapoBot) {
    private val commands = listOf(
        Help(config, this),
        Request(database),
        Shutdown(player, bot),
        Suggestions(),
        Test(),
        Veto(database, player),
        WhoAreYou()
    ).associateBy(Command::name)

    operator fun get(name: String) = commands[name]
}
