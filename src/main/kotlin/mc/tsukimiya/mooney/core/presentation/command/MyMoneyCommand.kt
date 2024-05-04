package mc.tsukimiya.mooney.core.presentation.command

import mc.tsukimiya.mooney.core.MooneyCore
import mc.tsukimiya.mooney.core.config.SystemAccount
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MyMoneyCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        val id = if (sender is Player) sender.uniqueId else SystemAccount.ADMIN.account.id
        val money = MooneyCore.api.getMoney(id)
        sender.sendMessage(MooneyCore.formatter.formatMessage("message.mymoney.success", sender.name, money.toString()))
        return true
    }
}
