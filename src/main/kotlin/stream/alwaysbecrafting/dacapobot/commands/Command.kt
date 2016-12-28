interface Command {
    val name: String
    val isDebugOnly: Boolean get() = false
    fun execute(user: String, args: String): String?
}

