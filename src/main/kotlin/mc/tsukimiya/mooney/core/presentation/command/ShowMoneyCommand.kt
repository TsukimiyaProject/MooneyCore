package mc.tsukimiya.mooney.core.presentation.command

import mc.tsukimiya.mooney.core.MooneyCore
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

class ShowMoneyCommand : TabExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args == null || args.isEmpty()) {
            sender.sendMessage(MooneyCore.formatter.formatMessage("message.general.arg-not-enough"))
            return false
        }

        val target = MooneyCore.api.getID(args[0])
        if (target == null) {
            sender.sendMessage(MooneyCore.formatter.formatMessage("message.general.not-found-data", args[0]))
            return true
        }

        val amount = MooneyCore.api.getMoney(target)
        sender.sendMessage(MooneyCore.formatter.formatMessage("message.showmoney.success", args[0], amount.toString()))

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
