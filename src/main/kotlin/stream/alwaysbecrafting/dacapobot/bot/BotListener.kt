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

        val nick = event.user?.nick ?: return
        val message = event.message ?: return
        database.logChat(nick, message)

        if (!message.startsWith(config.commandPrefix)) {
            return
        }

        val strippedCommand = message.substring(config.commandPrefix.length)
        val command: List<String> = strippedCommand.split(Regex("\\s+"), 2)
        val commandName = command[0]
        val args = command.elementAtOrElse(1, { "" })

        val response = commandRegistry[commandName]?.execute(nick, args)
        if (response != null) {
            event.respondWith(response)
        }
    }
}