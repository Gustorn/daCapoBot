import java.util.concurrent.ThreadLocalRandom

class Request(private val database: Database) : Command() {
    override val name = "request"
    override val help = "Usage: !request {title} or !request --random {title}"

    override fun executeInternal(user: String, args: CommandArgs) = when {
        args.flags.contains("--random") -> addRandomRequest(user, args.parameter)
        args.parameter != null          -> addConcreteRequest(user, args.parameter)
        else -> help
    }

    private fun addRandomRequest(user: String, partialTitle: String?): String {
        if (partialTitle == null) {
            val track = database.getRandomTrack() ?: return "Cannot add track to the queue at this time"
            database.addRequest(user, track.id)
            return "${track.title} added to the queue."
        } else {
            val tracks = database.findByTitle(partialTitle)
            val randomIndex = ThreadLocalRandom.current().nextInt(tracks.size)
            database.addRequest(user, tracks[randomIndex].id)
            return "${tracks[randomIndex].title} added to the queue."
        }
    }

    private fun addConcreteRequest(user: String, partialTitle: String): String {
        val lastRequestId = database.getLastRequestId()
        val tracks = database.findByTitle(partialTitle)

        return when (tracks.size) {
            0 -> "Sorry, I couldn't find any tracks containing $partialTitle"
            1 -> if (lastRequestId != null && lastRequestId == tracks[0].id) {
                "${tracks[0].title} is the last song in the request list. Please choose a different track"
            } else {
                database.addRequest(user, tracks[0].id)
                return "${tracks[0].title} added to the queue."
            }
            else -> joinTruncateAfter(tracks, 3, " | ", { it.title })
        }
    }
}

