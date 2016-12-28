import org.pircbotx.Configuration
import org.pircbotx.PircBotX
import org.pircbotx.cap.EnableCapHandler
import java.nio.charset.StandardCharsets

class DaCapoBot(config: Config,
                player: Player,
                database: Database,
                commandRegistry: CommandRegistry) {
    private val bot: PircBotX

    init {
        val botConfig = Configuration.Builder()
            .setAutoNickChange(false)
            .setOnJoinWhoEnabled(false)
            .setCapEnabled(true)
            .addCapHandler(EnableCapHandler("twitch.tv/membership"))
            .setEncoding(StandardCharsets.UTF_8)
            .addServer(config.ircServer)
            .setName(config.botName)
            .setServerPassword(config.oauth)
            .addAutoJoinChannel(config.ircChannel)
            .addListener(BotListener(config, player, database, commandRegistry))
            .buildConfiguration()
        bot = PircBotX(botConfig)
    }

    fun start() {
        bot.startBot()
    }
}
