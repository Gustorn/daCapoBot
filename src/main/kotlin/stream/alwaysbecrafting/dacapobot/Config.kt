import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Paths
import java.util.Properties

class Config(propertiesPath: String) {
    val musicDirectory: File
    val liveTrackFile: File
    val databaseFile: File
    val botName: String
    val ircServer: String
    val ircChannel: String
    val oauth: String
    val commandPrefix: String
    val admins: Set<String>

    private val defaultProperties = mapOf(
        "music_directory" to Paths.get(System.getProperty("user.home"), "Music").toString(),
        "live_track_file" to "location where the current track title is stored",
        "database_file"   to "dacapobot.db",
        "bot_name"        to "YOUR_BOT_NAME_HERE",
        "irc_server"      to "irc.twitch.tv",
        "irc_channel"     to "#test",
        "oauth"           to "YOUR_OAUTH_TOKEN_HERE",
        "command_prefix"  to "!",
        "admins"          to "comma delimited admin list here"
    )

    init {
        val propertiesFile = File(propertiesPath)
        val props = if (propertiesFile.exists()) {
            readConfiguration(propertiesFile)
        } else {
            generateDefaultProperties(propertiesFile)
        }
        musicDirectory     = File(props.getProperty("music_directory"))
        liveTrackFile      = File(props.getProperty("live_track_file"))
        databaseFile       = File(props.getProperty("database_file"))
        botName            = props.getProperty("bot_name")
        ircServer          = props.getProperty("irc_server")
        ircChannel         = props.getProperty("irc_channel")
        oauth              = props.getProperty("oauth")
        commandPrefix      = props.getProperty("command_prefix")
        admins             = props.getProperty("admins").split(",").map(String::trim).map(String::toLowerCase).toSet()
    }


    private fun generateDefaultProperties(file: File): Properties {
        val props = createProperties(defaultProperties.entries)
        FileOutputStream(file).use { props.store(it, null) }
        return props
    }

    private fun readConfiguration(file: File): Properties {
        val props = Properties()
        FileInputStream(file).use { props.load(it) }

        if (!props.stringPropertyNames().containsAll(defaultProperties.keys)) {
            System.err.println("Error: Missing Property in " + file.canonicalPath + "\n\t\tDelete to generate defaults.")
            System.exit(0)
        }
        return props
    }
}
