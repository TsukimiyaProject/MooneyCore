package mc.tsukimiya.mooney.core.command

import mc.tsukimiya.lib4b.command.BaseSubCommand
import mc.tsukimiya.lib4b.command.Validation
import mc.tsukimiya.lib4b.lang.MessageFormatter
import mc.tsukimiya.mooney.core.MooneyCore
import mc.tsukimiya.mooney.core.exception.AccountNotFoundException
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class MoneyPlusCommand(private val formatter: MessageFormatter) : BaseSubCommand(
    "plus",
    formatter.formatMessage("command.plus.usage"),
    "tsukimiya.mooney.core.plus"
) {
    override fun onRun(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args == null || args.size != 2 || !Validation.isNaturalNumber(args[1])) {
            return false
        }

        val target = Bukkit.getPlayerUniqueId(args[0])
        if (target == null) {
            sender.sendMessage(formatter.formatMessage("command.general.not-found-player", args[0]))
            return true
        }

        val amount = args[1].toULong()
        try {
            MooneyCore.instance.increaseMoney(target, amount)
            sender.sendMessage(formatter.formatMessage("command.plus.success", args[0], args[1]))
        } catch (e: AccountNotFoundException) {
            sender.sendMessage(formatter.formatMessage("command.general.not-found-player", args[0]))
        } catch (e: IllegalArgumentException) {
            sender.sendMessage(formatter.formatMessage("command.general.negative-amount"))
        }

        return true
    }
}
