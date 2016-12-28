import java.io.File

interface Database {
    fun logChat(user: String, message: String)
    fun addMusicFromDirectory(directory: File)

    fun addRequest(user: String, request: String): List<Track>
    fun addVeto(user: String, request: String): List<Track>

    fun getLastRequestId(): Int?
    fun getRandomTrack(): Track?
    fun getNextRequested(timestamp: Long): Track?
}
