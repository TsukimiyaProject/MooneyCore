package mc.tsukimiya.mooney.core.bukkit.command

import mc.tsukimiya.commandframework.SubCommandBase
import mc.tsukimiya.mooney.core.MooneyCore
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PayCommand(private val plugin: MooneyCore) : SubCommandBase("pay", "mooneycore.commands.money.pay") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.size < 2 || sender !is Player) {
            return false
        }

        val target = Bukkit.getOfflinePlayer(args[0])
        val amount = args[1].toIntOrNull() ?: return false

        if (!plugin.hasAccount(target)) {
            sender.sendMessage(plugin.messages.getString("no-data")!!.format(args[0]))
            return true
        }

        if (!plugin.has(sender, amount.toDouble())) {
            sender.sendMessage(plugin.messages.getString("no-money")!!)
            return true
        }

        val reason = args.getOrNull(2) ?: plugin.messages.getString("log.write.pay")!!.format(sender.name, target.name)

        val response = plugin.pay(sender, target, amount.toDouble(), reason)
        if (response.first.type == EconomyResponse.ResponseType.SUCCESS) {
            sender.sendMessage(
                plugin.messages.getString("pay")!!.format(
                    target.name,
                    plugin.format(response.first.amount),
                    plugin.currencyNameSingular(),
                    plugin.format(response.first.balance),
                    plugin.currencyNameSingular()
                )
            )
        } else {
            sender.sendMessage(plugin.messages.getString("error")!!.format("/money pay ${args[0]} ${args[1]}"))
        }

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String?> {
        val list = mutableListOf<String>()
        if (args.size == 1) {
            Bukkit.getOfflinePlayers().filter { it.name?.startsWith(args[0]) ?: false }.forEach {
                list.add(it.name!!)
            }
        }
        return list
    }
}
