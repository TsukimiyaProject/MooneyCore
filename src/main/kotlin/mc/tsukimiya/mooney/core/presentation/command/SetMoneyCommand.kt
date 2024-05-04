package mc.tsukimiya.mooney.core.presentation.command

import mc.tsukimiya.mooney.core.MooneyCore
import mc.tsukimiya.mooney.core.config.SystemAccount
import mc.tsukimiya.mooney.core.exception.MoneyOverLowerLimitException
import mc.tsukimiya.mooney.core.exception.MoneyOverUpperLimitException
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class SetMoneyCommand : TabExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args == null || args.size < 2) {
            sender.sendMessage(MooneyCore.formatter.formatMessage("message.general.arg-not-enough"))
            return false
        }

        val target = MooneyCore.api.getID(args[0])
        if (target == null) {
            sender.sendMessage(MooneyCore.formatter.formatMessage("message.general.not-found-data", args[0]))
            return true
        }

        if (!MooneyCore.api.validateMoney(args[1])) {
            sender.sendMessage(MooneyCore.formatter.formatMessage("message.general.validate-money"))
            return false
        }

        if (args.size > 2 && MooneyCore.api.validateReason(args[2])) {
            sender.sendMessage(MooneyCore.formatter.formatMessage("message.general.validate-reason"))
            return false
        }

        val amount = args[1].toLong()
        val executor = if (sender is Player) sender.name else SystemAccount.ADMIN.account.name.value
        val reason = if (args.size > 2) args[2] else MooneyCore.formatter.formatMessage("log.set", executor)
        try {
            MooneyCore.api.storeAccount(target, money = amount, reason = reason)
            sender.sendMessage(MooneyCore.formatter.formatMessage("message.setmoney.success", args[0], args[1]))
        } catch (e: MoneyOverUpperLimitException) {
            sender.sendMessage(MooneyCore.formatter.formatMessage("message.general.over-upper", args[1]))
        } catch (e: MoneyOverLowerLimitException) {
            sender.sendMessage(MooneyCore.formatter.formatMessage("message.general.over-lower"))
        }

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): MutableList<String>? {
        if (args?.size == 1) {
            val list = mutableListOf<String>()
            Bukkit.getOfflinePlayers().forEach {
                list.add(it.name!!)
            }
            return list
        }

        return null
    }
}
