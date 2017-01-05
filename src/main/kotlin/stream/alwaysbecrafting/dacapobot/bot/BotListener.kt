import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.ConnectEvent
import org.pircbotx.hooks.events.MessageEvent

class BotListener(private val config: Config,
                  private val player: Player,
                  private val database: Database,
                  private val commandRegistry : CommandRegistry) : ListenerAdapter() {
    override fun onConnect(event: ConnectEvent) {
        super.onConnect(event)
        database.addMusicFromDirectory(config.musicDirectory)
        player.setQueue()
        player.play()
    }

    override fun onMessage(event: MessageEvent) {
        super.onMessage(event)

        val user = event.user?.nick ?: return
        val message = event.message ?: return
        database.logChat(user, message)

        val (commandName, rawArgs) = extractCommand(message, config) ?: return
        val response = commandRegistry[commandName]?.execute(user, rawArgs, config) ?: return
        event.respondWith(response)
    }
}