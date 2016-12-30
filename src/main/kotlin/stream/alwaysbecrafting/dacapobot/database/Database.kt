import java.io.File

interface Database {
    fun logChat(user: String, message: String)
    fun addMusicFromDirectory(directory: File)

    fun addRequest(user: String, trackId: Int)
    fun addVeto(user: String, trackId: Int)

    fun getRandomTrack(): Track?
    fun getLastRequestId(): Int?
    fun getNextRequested(timestamp: Long): Track?
    fun getMatchingTracks(partialTitle: String): List<Track>
}
