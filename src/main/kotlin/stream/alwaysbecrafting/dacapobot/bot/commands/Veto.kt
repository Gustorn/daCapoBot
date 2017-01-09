class Veto(private val database: Database, private val player: Player) : Command() {
    override val name = "veto"
    override val help = "Usage: !veto {title}"

    override fun executeInternal(user: String, args: CommandArgs): String? {
        val title = args.parameter ?: return help
        val tracks = database.findByTitle(title)

        return when (tracks.size) {
            0 -> "Sorry, I couldn't find any tracks containing $title"
            1 -> if (player.currentTrack?.id == tracks[0].id) {
                    database.addVeto(user, tracks[0].id)
                    player.nextTrack()
                    "${tracks[0].title} vetoed, thank you."
                } else {
                    null
                }
            else -> tracks.joinToString(truncateAfter = 3, separator = " | ", mapper = { it.title })
        }
    }
}
