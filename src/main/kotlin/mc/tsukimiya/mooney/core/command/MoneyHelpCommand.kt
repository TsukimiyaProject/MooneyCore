package mc.tsukimiya.mooney.core.command

import mc.tsukimiya.lib4b.command.BaseSubCommand
import mc.tsukimiya.lib4b.lang.MessageFormatter
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class MoneyHelpCommand(private val formatter: MessageFormatter) : BaseSubCommand(
    "help",
    formatter.formatMessage("command.help.usage"),
    "tsukimiya.mooney.core.help"
) {
    override fun onRun(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        // 怒らないでね
        sender.sendMessage(formatter.formatMessage("command.help.header"))
        sender.sendMessage(formatter.formatMessage("command.help.money"))
        sender.sendMessage(formatter.formatMessage("command.help.pay"))
        if (sender.hasPermission("tsukimiya.mooney.core.help.op")) {
            sender.sendMessage(formatter.formatMessage("command.help.show"))
            sender.sendMessage(formatter.formatMessage("command.help.set"))
            sender.sendMessage(formatter.formatMessage("command.help.plus"))
            sender.sendMessage(formatter.formatMessage("command.help.minus"))
        }
        return true
    }
}
