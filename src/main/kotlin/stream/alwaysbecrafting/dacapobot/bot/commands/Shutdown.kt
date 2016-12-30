class Shutdown(private val player : Player, private val bot : DaCapoBot) : Command() {
    override val name = "shutdown"
    override val help = "Stops the music and cleanly stops the bot"
    override val permission = CommandPermission.ADMIN

    override fun executeInternal(user: String, args: String?): String? {
        player.stop()
        bot.shutdown()
        return null
    }
}
