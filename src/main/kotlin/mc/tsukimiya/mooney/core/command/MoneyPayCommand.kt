package mc.tsukimiya.mooney.core.command

import mc.tsukimiya.lib4b.command.BaseSubCommand
import mc.tsukimiya.lib4b.command.Validation
import mc.tsukimiya.lib4b.lang.MessageFormatter
import mc.tsukimiya.mooney.core.MooneyCoreAPI
import mc.tsukimiya.mooney.core.exception.AccountNotFoundException
import mc.tsukimiya.mooney.core.exception.InvalidMoneyAmountException
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MoneyPayCommand(private val formatter: MessageFormatter) : BaseSubCommand(
    "pay",
    formatter.formatMessage("command.pay.usage"),
    "tsukimiya.mooney.core.pay"
) {
    override fun onRun(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args == null || args.size != 2 || !Validation.isInt(args[1])) {
            return false
        }

        val target = Bukkit.getPlayerUniqueId(args[0])
        if (target == null) {
            sender.sendMessage(formatter.formatMessage("command.general.not-found-player", args[0]))
            return true
        }

        val amount = args[1].toInt()
        try {
            MooneyCoreAPI.payMoney((sender as Player).uniqueId, target, amount)
            sender.sendMessage(formatter.formatMessage("command.pay.success", args[0], args[1]))
        } catch (e: AccountNotFoundException) {
            sender.sendMessage(formatter.formatMessage("command.general.not-found-player", args[0]))
        } catch (e: InvalidMoneyAmountException) {
            sender.sendMessage(formatter.formatMessage("command.general.less-than-zero", sender.name))
        } catch (e: IllegalArgumentException) {
            sender.sendMessage(formatter.formatMessage("command.general.negative-amount"))
        }

        return true
    }

    override fun isRestrictConsole(): Boolean {
        return true
    }
}
