class Veto(private val database: Database, private val player: Player) : Command {
    override val name = "veto"

    override fun execute(user: String, args: String): String? {
        val tracks = database.addVeto(user, args)

        return when (tracks.size) {
            0 -> "Sorry, I couldn't find any tracks containing $args"
            1 -> if (player.currentTrack?.id == tracks[0].id) {
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
