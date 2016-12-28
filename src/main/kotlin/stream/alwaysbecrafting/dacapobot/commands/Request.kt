class Request(private val database: Database) : Command {
    override val name = "request"

    override fun execute(user: String, args: String): String? {
        val lastRequestId = database.getLastRequestId()
        val tracks = database.addRequest(user, args)

        return when (tracks.size) {
            0 -> "Sorry, I couldn't find any tracks containing $args"
            1 -> if (lastRequestId != null && lastRequestId == tracks[0].id) {
               "${tracks[0].title} is the last song in the request list. Please choose a different track"
            } else {
                return "${tracks[0].title} added to the queue."
            }
            else -> {
                var response = tracks
                    .slice(0 until Math.min(3, tracks.size))
                    .map { it.title }
                    .joinToString(" | ")
                if (tracks.size > 3) {
                    response += " | +${tracks.size - 3} more"
                }
                response
            }
        }
    }
}

