package mc.tsukimiya.mooney.core.bukkit.command

import mc.tsukimiya.commandframework.SubCommandBase
import mc.tsukimiya.mooney.core.MooneyCore
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class HelpCommand(private val plugin: MooneyCore) : SubCommandBase("help", "mooneycore.commands.money.help") {
    companion object {
        private val messages = mapOf(
            "mooneycore.commands.money.help" to "help.header",
            "mooneycore.commands.money.show" to "help.money",
            "mooneycore.commands.money.show.other" to "help.money-other",
            "mooneycore.commands.money.pay" to "help.pay",
            "mooneycore.commands.money.give" to "help.give",
            "mooneycore.commands.money.take" to "help.take",
            "mooneycore.commands.money.log" to "help.log",
            "mooneycore.commands.money.log.other" to "help.log-other"
        )
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        messages.filter { sender.hasPermission(it.key) }.values.forEach {
            sender.sendMessage(plugin.messages.getString(it)!!)
        }

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String?>? {
        return null
    }
}
