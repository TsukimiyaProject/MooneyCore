package mc.tsukimiya.mooney.core.command

import mc.tsukimiya.lib4b.command.BaseSubCommand
import mc.tsukimiya.mooney.core.MooneyCore
import mc.tsukimiya.mooney.core.exception.AccountNotFoundException
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class MoneyShowCommand : BaseSubCommand(
    "show",
    MooneyCore.instance.formatter.formatMessage("command.show.usage"),
    "tsukimiya.mooney.core.show"
) {
    override fun onRun(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args == null || args.isEmpty()) {
            return false
        }

        val target = Bukkit.getPlayerUniqueId(args[0])
        if (target == null) {
            sender.sendMessage(MooneyCore.instance.formatter.formatMessage("command.general.not-found-player", args[0]))
            return true
        }

        try {
            val amount = MooneyCore.instance.getMoney(target)
            sender.sendMessage(
                MooneyCore.instance.formatter.formatMessage("command.show.success", args[0], amount.toString())
            )
        } catch (e: AccountNotFoundException) {
            sender.sendMessage(MooneyCore.instance.formatter.formatMessage("command.general.not-found-player", args[0]))
        }

        return true
    }
}
