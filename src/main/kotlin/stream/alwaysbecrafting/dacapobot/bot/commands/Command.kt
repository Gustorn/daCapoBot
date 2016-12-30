import java.util.*

enum class CommandPermission(val flag: Int) {
    NORMAL(0x1),
    ADMIN (NORMAL.flag or 0x2);

    fun canExecute(user: String, config: Config): Boolean {
        val userPermission = if (config.admins.contains(user.toLowerCase())) ADMIN else NORMAL
        return (this.flag and userPermission.flag) >= this.flag
    }
}

abstract class Command {
    abstract val name: String
    abstract val help: String
    open val permission: CommandPermission get() = CommandPermission.NORMAL

    fun execute(user: String, args: String?, config: Config): String? = if (!permission.canExecute(user, config)) {
        "I'm sorry $user, I'm afraid I can't do that."
    } else {
        executeInternal(user, args)
    }

    protected abstract fun executeInternal(user: String, args: String?): String?
}

fun extractCommand(request: String, config: Config, strictMatching: Boolean = true): Pair<String, String?>? {
    val trimmedRequest = request.trim()
    return if (strictMatching && !trimmedRequest.startsWith(config.commandPrefix)) {
        null
    } else {
        val strippedRequest = trimmedRequest.substring(config.commandPrefix.length)
        val command = strippedRequest.split(Regex("\\s+"), 2)
        val commandName = command[0]
        val commandArgs = command.elementAtOrNull(1)
        return Pair(commandName, commandArgs)
    }
}

fun splitFlagsAndArguments(args: String): Pair<List<String>, String?> {
    val flags = ArrayList<String>()

    var rest: String? = args.trimStart()
    while (rest != null && rest.startsWith("--")) {
        val split = rest.split(Regex("\\s+"), 2)
        flags.add(split[0])
        rest = split.elementAtOrNull(1)
    }
    return Pair(flags, rest)
}
