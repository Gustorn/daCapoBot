import java.io.File

import com.mpatric.mp3agic.ID3v1
import com.mpatric.mp3agic.Mp3File

class TrackData(private val file: File) {
    val title: String
    val artist: String
    val album: String

    val path: String by lazy { file.canonicalPath }

    init {
        val file = Mp3File(file)
        val tag: ID3v1? = if (file.hasId3v1Tag()) {
            file.id3v1Tag
        } else if (file.hasId3v2Tag()) {
            file.id3v2Tag
        } else {
            null
        }

        title  = tag?.title ?: ""
        artist = tag?.artist ?: ""
        album  = tag?.album ?: ""
    }
}