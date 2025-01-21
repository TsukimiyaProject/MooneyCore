package mc.tsukimiya.mooney.core.bukkit.command

import mc.tsukimiya.commandframework.SubCommandBase
import mc.tsukimiya.mooney.core.MooneyCore
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class TakeCommand(private val plugin: MooneyCore) : SubCommandBase("take", "mooneycore.commands.money.take") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.size < 2) {
            return false
        }

        val target = Bukkit.getOfflinePlayer(args[0])
        val amount = args[1].toIntOrNull() ?: return false

        if (!plugin.vault.hasAccount(target)) {
            sender.sendMessage(plugin.messages.getString("no-data")!!.format(args[0]))
            return true
        }

        val response = plugin.vault.withdrawPlayer(target, amount.toDouble())
        if (response.type == EconomyResponse.ResponseType.SUCCESS) {
            sender.sendMessage(
                plugin.messages.getString("take")!!.format(
                    target.name,
                    plugin.vault.format(response.amount),
                    plugin.vault.currencyNameSingular(),
                    target.name,
                    plugin.vault.format(response.balance),
                    plugin.vault.currencyNameSingular()
                )
            )
        } else {
            sender.sendMessage(plugin.messages.getString("error")!!.format("/money take ${args[0]} ${args[1]}"))
        }

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String?>? {
        val list = mutableListOf<String>()
        if (args.size == 1) {
            Bukkit.getOfflinePlayers().filter { it.name?.startsWith(args[0]) ?: false }.forEach {
                list.add(it.name!!)
            }
        }
        return list
    }
}
