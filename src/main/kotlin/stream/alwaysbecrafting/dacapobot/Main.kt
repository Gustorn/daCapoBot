fun main(args: Array<String>) {
    val config = Config("dacapobot.properties")
    val database = SQLiteDatabase(config)
    val player = JFXPlayer(config, database)
    val bot = DaCapoBot(config, player, database)

    bot.start()
}
