package mc.tsukimiya.mooney.core.command

import mc.tsukimiya.lib4b.command.BaseCommand
import mc.tsukimiya.lib4b.lang.MessageFormatter
import mc.tsukimiya.mooney.core.MooneyCore
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MoneyCommand(private val formatter: MessageFormatter) : BaseCommand("money") {
    override fun onRun(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        val money = MooneyCore.instance.getMoney((sender as Player).uniqueId)
        sender.sendMessage(formatter.formatMessage("command.money.success", money.toString()))
        return true
    }

    override fun isRestrictConsole(): Boolean = true
}
