import java.util.concurrent.ThreadLocalRandom

class Request(private val database: Database) : Command() {
    override val name = "request"
    override val help = "Usage: !request {title} or !request --random {title}"

    override fun executeInternal(user: String, args: String?): String? {
        val requestArgs = args ?: return help
        val (flags, maybeTitle) = splitFlagsAndArguments(requestArgs)
        val title = maybeTitle ?: return help

        return if (flags.size == 1 && flags[0] == "--random") {
            addRandomRequest(user, title)
        } else {
            addConcreteRequest(user, title)
        }
    }

    private fun addRandomRequest(user: String, partialTitle: String): String? {
        val tracks = database.getMatchingTracks(partialTitle)
        val randomIndex = ThreadLocalRandom.current().nextInt(tracks.size)
        database.addRequest(user, tracks[randomIndex].id)
        return "${tracks[randomIndex].title} added to the queue."
    }

    private fun addConcreteRequest(user: String, partialTitle: String): String? {
        val lastRequestId = database.getLastRequestId()
        val tracks = database.getMatchingTracks(partialTitle)

        return when (tracks.size) {
            0 -> "Sorry, I couldn't find any tracks containing $partialTitle"
            1 -> if (lastRequestId != null && lastRequestId == tracks[0].id) {
                "${tracks[0].title} is the last song in the request list. Please choose a different track"
            } else {
                database.addRequest(user, tracks[0].id)
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

