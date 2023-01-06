package mc.tsukimiya.mooney.core.command

import mc.tsukimiya.lib4b.command.BaseCommand
import mc.tsukimiya.mooney.core.MooneyCore
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MoneyCommand : BaseCommand("money") {
    override fun onRun(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        val money = MooneyCore.instance.getMoney((sender as Player).uniqueId)
        sender.sendMessage("所持金は${money}です")
        return true
    }

    override fun isRestrictConsole(): Boolean = true
}
