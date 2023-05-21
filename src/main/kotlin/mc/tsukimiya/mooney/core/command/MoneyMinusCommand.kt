package mc.tsukimiya.mooney.core.command

import mc.tsukimiya.lib4b.command.BaseSubCommand
import mc.tsukimiya.lib4b.command.Validation
import mc.tsukimiya.mooney.core.MooneyCore
import mc.tsukimiya.mooney.core.exception.InvalidMoneyAmountException
import mc.tsukimiya.mooney.core.exception.AccountNotFoundException
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class MoneyMinusCommand : BaseSubCommand(
    "minus",
    MooneyCore.instance.formatter.formatMessage("command.minus.usage"),
    "tsukimiya.mooney.core.minus"
) {
    override fun onRun(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args == null || args.size != 2 || !Validation.isInt(args[1])) {
            return false
        }

        val target = Bukkit.getPlayerUniqueId(args[0])
        if (target == null) {
            sender.sendMessage(MooneyCore.instance.formatter.formatMessage("command.general.not-found-player", args[0]))
            return true
        }

        val amount = args[1].toInt()
        try {
            MooneyCore.instance.decreaseMoney(target, amount)
            sender.sendMessage(MooneyCore.instance.formatter.formatMessage("command.minus.success", args[0], args[1]))
        } catch (e: AccountNotFoundException) {
            sender.sendMessage(MooneyCore.instance.formatter.formatMessage("command.general.not-found-player", args[0]))
        } catch (e: InvalidMoneyAmountException) {
            sender.sendMessage(MooneyCore.instance.formatter.formatMessage("command.general.less-than-zero", args[0]))
        } catch (e: IllegalArgumentException) {
            sender.sendMessage(MooneyCore.instance.formatter.formatMessage("command.general.negative-amount"))
        }

        return true
    }
}
