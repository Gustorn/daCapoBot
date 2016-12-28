import java.io.File

class Track(val id: Int,
            val timestamp: Long,
            val title: String,
            val album: String,
            val artist: String,
            private val file: File) {
    val uri: String by lazy { file.toURI().toString() }
    val exists: Boolean get() = file.exists()
    val debugString: String by lazy {
        val builder = StringBuilder()
        builder.appendln(id)
        builder.appendln(timestamp)
        builder.appendln(title)
        builder.appendln(album)
        builder.appendln(artist)
        builder.appendln(file)
        builder.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        return id == (other as Track).id
    }

    override fun hashCode(): Int = id
    override fun toString(): String = file.name
}
