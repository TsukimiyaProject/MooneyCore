package mc.tsukimiya.mooney.core.command

import org.bukkit.plugin.java.JavaPlugin

class CommandRegister(private val plugin: JavaPlugin) {
    fun registerCommand(baseCommand: BaseCommand) {
        val command = plugin.getCommand(baseCommand.name)
        command?.setExecutor(baseCommand)
        command?.tabCompleter = baseCommand
    }
}
