import java.nio.charset.StandardCharsets

import org.pircbotx.Configuration
import org.pircbotx.PircBotX
import org.pircbotx.cap.EnableCapHandler

class DaCapoBot(config: Config,
                player: Player,
                database: Database) {
    private val bot: PircBotX

    init {
        val commands = CommandRegistry(config, database, player, this)
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
            .addListener(BotListener(config, player, database, commands))
            .buildConfiguration()
        bot = PircBotX(botConfig)
    }

    fun start() {
        bot.startBot()
    }

    fun shutdown() {
        bot.send().quitServer()
    }
}
