package mc.tsukimiya.mooney.core.bukkit.command

import mc.tsukimiya.commandframework.CommandBase
import mc.tsukimiya.mooney.core.MooneyCore
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MoneyCommand(private val plugin: MooneyCore) : CommandBase() {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: List<String>): Boolean {
        var target: OfflinePlayer? = sender as? Player
        if (args.getOrNull(0) != null && sender.hasPermission("mooneycore.commands.money.show.other")) {
            target = Bukkit.getOfflinePlayer(args[0])
        }
        if (target == null) {
            return false
        }

        if (!plugin.vault.hasAccount(target)) {
            sender.sendMessage(plugin.messages.getString("no-data")!!.format(args[0]))
            return true
        }

        val money = plugin.vault.format(plugin.vault.getBalance(target))
        sender.sendMessage(
            plugin.messages.getString("money")!!.format(target.name, money, plugin.vault.currencyNameSingular())
        )

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: List<String>
    ): List<String>? {
        val list = mutableListOf<String>()
        if (args.size == 1 && sender.hasPermission("mooneycore.commands.money.show.other")) {
            Bukkit.getOfflinePlayers().filter { it.name?.startsWith(args[0]) ?: false }.forEach {
                list.add(it.name!!)
            }
        }
        return list
    }
}
