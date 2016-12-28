import javafx.embed.swing.JFXPanel
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import java.io.PrintWriter

class JFXPlayer(private val config: Config, private val database : Database) : Player {
    override var currentTrack: Track? = null
    private var player: MediaPlayer? = null
    private var timestamp: Long = System.currentTimeMillis()

    init {
        JFXPanel()
    }

    override fun setQueue() {
        currentTrack = findExistingTrack { database.getRandomTrack() }
        System.out.println( "queue " + this.currentTrack )
    }

    override fun play() {
        val track = currentTrack ?: return

        player?.stop()
        player = MediaPlayer(Media(track.uri))
        System.out.println("Now Playing: ${track.title}")
        config.liveTrackFile.printWriter().use { it.println(track.title) }

        player?.play()
        player?.setOnEndOfMedia { nextTrack() }
    }

    override fun nextTrack() {
        currentTrack = findExistingTrack {
            val requestedTrack = database.getNextRequested(timestamp)
            if (requestedTrack != null) {
                timestamp = requestedTrack.timestamp
                requestedTrack
            } else {
                timestamp = System.currentTimeMillis()
                database.getRandomTrack()
            }
        }
        play()
    }

    private fun findExistingTrack(fn: () -> Track?): Track {
        var track: Track? = null
        while (track == null || !track.exists) {
            track = fn()
        }
        return track
    }
}
