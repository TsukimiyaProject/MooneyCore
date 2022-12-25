package mc.tsukimiya.mooney.core.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

abstract class BaseCommand(val name: String) : TabExecutor, CommandRunnable, RestrictSenderTrait {
    private val subCommands = mutableMapOf<String, BaseSubCommand>()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args != null && args.isNotEmpty()) {
            val subCommand = subCommands.getOrDefault(args[0], null)
            if (subCommand != null) {
                if (isRestriction(sender) || !subCommand.testPermission(sender)) {
                    return true
                }

                return subCommand.onRun(sender, command, args[0], args.dropLast(args.size - 1).toTypedArray())
            }
        }

        if (isRestriction(sender)) {
            return true
        }
        return onRun(sender, command, label, args)
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): MutableList<String>? {
        if (args?.size == 1) {
            return subCommands.keys.filter { it.startsWith(args[0]) }.toMutableList()
        }

        return null
    }

    fun registerSubCommands(vararg commands: BaseSubCommand) {
        commands.forEach {command->
            (command.aliases + command.name).distinct().forEach { key ->
                if (!subCommands.containsKey(key)) {
                    subCommands[key] = command
                } else {
                    throw RuntimeException("SubCommand with same name / alias for '$key' already exists")
                }
            }
        }
    }
}
