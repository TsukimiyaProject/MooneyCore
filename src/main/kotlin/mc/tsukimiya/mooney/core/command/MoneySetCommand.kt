package mc.tsukimiya.mooney.core.command

import mc.tsukimiya.lib4b.command.BaseSubCommand
import mc.tsukimiya.lib4b.command.Validation
import mc.tsukimiya.mooney.core.MooneyCore
import mc.tsukimiya.mooney.core.exception.WalletNotFoundException
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class MoneySetCommand : BaseSubCommand("set", "/money set <player> <amount>", "tsukimiya.mooney.core.set") {
    override fun onRun(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args == null || args.size < 2 || Validation.isInt(args[1])) {
            return false
        }

        val target = Bukkit.getPlayerUniqueId(args[0])
        if (target == null) {
            sender.sendMessage(args[0] + "は存在しないプレイヤーです")
            return true
        }

        val amount = args[1].toInt()
        try {
            MooneyCore.getInstance().setMoney(target, amount)
        } catch (e: WalletNotFoundException) {
            sender.sendMessage(args[0] + "のデータがありません")
        }

        return true
    }
}
