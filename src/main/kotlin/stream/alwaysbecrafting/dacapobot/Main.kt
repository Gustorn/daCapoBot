fun main(args: Array<String>) {
    val config = Config("dacapotbot.properties")
    val database = SQLiteDatabase(config)
    val player = JFXPlayer(config, database)
    val commands = CommandRegistry(config, database, player)
    val bot = DaCapoBot(config, player, database, commands)

    bot.start()
}
