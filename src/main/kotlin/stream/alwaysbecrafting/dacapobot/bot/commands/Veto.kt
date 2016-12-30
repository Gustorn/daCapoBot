class Veto(private val database: Database, private val player: Player) : Command() {
    override val name = "veto"
    override val help = "Usage: !veto {title}"

    override fun executeInternal(user: String, args: String?): String? {
        val title = args ?: return help
        val tracks = database.getMatchingTracks(title)

        return when (tracks.size) {
            0 -> "Sorry, I couldn't find any tracks containing $title"
            1 -> if (player.currentTrack?.id == tracks[0].id) {
                    database.addVeto(user, tracks[0].id)
                    player.nextTrack()
                    "${tracks[0].title} vetoed, thank you."
                } else {
                    null
                }
            else -> {
                var response = tracks
                    .slice(0 until Math.min(3, tracks.size))
                    .map{ it.title }
                    .joinToString(" | ")
                if (tracks.size > 3) {
                    response += " | +${tracks.size - 3} more"
                }
                response
            }
        }
    }
}
