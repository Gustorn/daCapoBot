import java.io.File
import java.nio.file.FileVisitOption
import java.nio.file.Files
import java.nio.file.Path
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.*
import java.util.stream.Collectors

import org.flywaydb.core.Flyway

class SQLiteDatabase(config: Config) : Database {
    private val connection: Connection

    private val loggingStatement: PreparedStatement
    private val lastRequestStatement: PreparedStatement
    private val addRequestStatement: PreparedStatement
    private val addVetoStatement: PreparedStatement
    private val matchingQueryStatement: PreparedStatement
    private val nextRequestQueryStatement: PreparedStatement
    private val randomTrackQueryStatement: PreparedStatement

    init {
        createOrUpgrade(config)
        connection = DriverManager.getConnection(getConnectionString(config))
        connection.autoCommit = true
        System.out.println("Connection to SQLite has been established.")

        val loggingSql = "INSERT INTO chat_log(timestamp,user,message) VALUES(?,?,?)"
        loggingStatement = connection.prepareStatement(loggingSql)

        val lastRequestSql = "SELECT id FROM requests ORDER BY id DESC LIMIT 1"
        lastRequestStatement = connection.prepareStatement(lastRequestSql)

        val addRequestSql = "INSERT INTO requests(timestamp, user, track_id) VALUES(?,?,?)"
        addRequestStatement = connection.prepareStatement(addRequestSql)

        val addVetoSql = "INSERT INTO vetoes(timestamp, user, track_id) VALUES(?,?,?)"
        addVetoStatement = connection.prepareStatement(addVetoSql)

        val matchingQuerySql = "SELECT * FROM tracks WHERE title LIKE ?"
        matchingQueryStatement = connection.prepareStatement(matchingQuerySql)

        val nextRequestQuerySql = """SELECT t.id, r.timestamp, t.title, t.album, t.artist, t.path
                                     FROM tracks AS t
                                     INNER JOIN requests AS r ON t.id = r.track_id
                                     WHERE r.timestamp > ? limit 1"""
        nextRequestQueryStatement = connection.prepareStatement(nextRequestQuerySql)

        val randomTrackQuerySql = "SELECT * FROM tracks WHERE id IN (SELECT id FROM tracks ORDER BY RANDOM() LIMIT 1)"
        randomTrackQueryStatement = connection.prepareStatement(randomTrackQuerySql)
    }

    override fun addMusicFromDirectory(directory: File) {
        System.out.println("Checking for tracks to insert...")
        val newTracks = getNewTracksFromDirectory(directory)

        connection.autoCommit = false
        val sql = "INSERT INTO tracks(title,path,artist,album) VALUES(?,?,?,?)"
        connection.executeUpdate(sql, { statement ->
            for (track in newTracks) {
                statement.setString(1, track.title)
                statement.setString(2, track.path)
                statement.setString(3, track.artist)
                statement.setString(4, track.album)
                statement.executeUpdate()
            }
        })
        connection.commit()
        connection.autoCommit = true
    }

    override fun logChat(user: String, message: String) {
        loggingStatement.setLong(1, System.currentTimeMillis())
        loggingStatement.setString(2, user)
        loggingStatement.setString(3, message)
        loggingStatement.executeUpdate()
    }

    override fun addRequest(user: String, trackId: Int) {
        addRequestStatement.setLong(1, System.currentTimeMillis())
        addRequestStatement.setString(2, user)
        addRequestStatement.setInt(3, trackId)
        addRequestStatement.execute()
    }

    override fun addVeto(user: String, trackId: Int) {
        addVetoStatement.setLong(1, System.currentTimeMillis())
        addVetoStatement.setString(2, user)
        addVetoStatement.setInt(3, trackId)
        addVetoStatement.execute()
    }

    override fun findByTitle(partialTitle: String): List<Track> {
        val fuzzyRequest = "%$partialTitle%".replace(Regex("[\\W_]+"), "%")
        matchingQueryStatement.setString(1, fuzzyRequest)
        val results = matchingQueryStatement.executeQuery()

        val tracks = ArrayList<Track>()
        while (results.next()) {
            val file = File(results.getString("path"))
            if (!file.exists())
                continue

            val track = createTrackFromResultSet(results, file)
            tracks.add(track)
            System.out.print(track.debugString)
        }
        return tracks
    }

    override fun getLastRequestId(): Int? {
        val results = lastRequestStatement.executeQuery()
        if (results.next()) {
            return results.getInt(1)
        } else {
            return null
        }
    }

    override fun getNextRequested(timestamp: Long): Track? {
        nextRequestQueryStatement.setLong( 1, timestamp )
        val results = nextRequestQueryStatement.executeQuery()
        if (!results.next()) {
            return null
        }

        val file = File(results.getString("path"))
        return createTrackFromResultSet(results, file, results.getLong("timestamp"))
    }

    override fun getRandomTrack(): Track? {
        val results = randomTrackQueryStatement.executeQuery()
        if (!results.next()) {
            return null
        }
        val file = File(results.getString("path"))
        return createTrackFromResultSet(results, file)
    }

    private fun createOrUpgrade(config: Config) {

        val flyway = Flyway()
        flyway.setDataSource(getConnectionString(config), "", "")
        flyway.migrate()
    }

    private fun getConnectionString(config: Config): String {
        val databasePath = config.databaseFile.canonicalPath
        return "jdbc:sqlite:$databasePath?journal_mode=WAL&synchronous=NORMAL&foreign_keys=ON"
    }

    private fun getExistingPaths(): Set<String> {
        val tracks = HashSet<String>()
        connection.processQuery("SELECT path from tracks", {
            tracks.add(it.getString(1))
        })
        return tracks
    }

    private fun getNewTracksFromDirectory(directory: File): List<TrackData> {
        val existingTracks = getExistingPaths()
        return Files.walk(directory.toPath(), FileVisitOption.FOLLOW_LINKS)
            .filter { it.toString().endsWith(".mp3") }
            .map(Path::toFile)
            .filter { !existingTracks.contains(it.canonicalPath) }
            .parallel()
            .map(::TrackData)
            .collect(Collectors.toList<TrackData>())
    }

    private fun createTrackFromResultSet(results: ResultSet, file: File, timestamp: Long = System.currentTimeMillis()): Track {
        return Track(results.getInt("id"),
                     timestamp,
                     results.getString("title"),
                     results.getString("album"),
                     results.getString("artist"),
                     file)
    }
}