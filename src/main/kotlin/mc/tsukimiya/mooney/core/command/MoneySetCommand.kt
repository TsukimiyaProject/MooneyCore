package mc.tsukimiya.mooney.core.command

import mc.tsukimiya.lib4b.command.BaseSubCommand
import mc.tsukimiya.lib4b.command.Validation
import mc.tsukimiya.mooney.core.MooneyCore
import mc.tsukimiya.mooney.core.exception.WalletNotFoundException
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class MoneySetCommand : BaseSubCommand(
    "set",
    MooneyCore.instance.formatter.formatMessage("command.set-money.usage"),
    "tsukimiya.mooney.core.set"
) {
    override fun onRun(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args == null || args.size < 2 || Validation.isInt(args[1])) {
            return false
        }

        val target = Bukkit.getPlayerUniqueId(args[0])
        if (target == null) {
            sender.sendMessage(MooneyCore.instance.formatter.formatMessage("command.general.not-found-player", args[0]))
            return true
        }

        val amount = args[1].toInt()
        try {
            MooneyCore.instance.setMoney(target, amount)
            sender.sendMessage(
                MooneyCore.instance.formatter.formatMessage("command.set-money.success", args[0], args[1])
            )
        } catch (e: WalletNotFoundException) {
            sender.sendMessage(MooneyCore.instance.formatter.formatMessage("command.general.not-found-player", args[0]))
        } catch (e: IllegalArgumentException) {
            sender.sendMessage(MooneyCore.instance.formatter.formatMessage("command.general.negative-amount"))
        }

        return true
    }
}
