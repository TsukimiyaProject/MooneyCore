package mc.tsukimiya.mooney.core.bukkit.command

import mc.tsukimiya.commandframework.SubCommandBase
import mc.tsukimiya.mooney.core.MooneyCore
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class LogCommand(private val plugin: MooneyCore) : SubCommandBase("log", "mooneycore.commands.money.log") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        var target: OfflinePlayer? = sender as? Player
        var page = args.getOrNull(0)?.toIntOrNull() ?: 1

        if (sender.hasPermission("mooneycore.commands.money.log.other")) {
            when (args.size) {
                1 -> {
                    if (args.getOrNull(0)?.toIntOrNull() == null) {
                        target = Bukkit.getOfflinePlayer(args[0])
                        page = 1
                    }
                }

                2 -> {
                    target = Bukkit.getOfflinePlayer(args[0])
                    page = args.getOrNull(1)?.toIntOrNull() ?: 1
                }
            }
        }

        if (target == null || !plugin.vault.hasAccount(target)) {
            sender.sendMessage(plugin.messages.getString("no-data")!!.format(args[0]))
            return true
        }

        val histories = plugin.vault.getHistories(target, page)!!

        sender.sendMessage(plugin.messages.getString("log.read.header")!!.format(page, histories.second))
        histories.first.forEach {
            sender.sendMessage(
                plugin.messages.getString("log.read.content")!!.format(
                    it.datetime,
                    it.datetime,
                    it.datetime,
                    it.reason?.value ?: "",
                    if (it.from?.owner == target.uniqueId) "-" else "+",
                    plugin.vault.format(it.amount.value.toDouble()),
                    plugin.vault.currencyNameSingular()
                )
            )
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
        if (args.size == 1 && sender.hasPermission("mooneycore.commands.money.log.other")) {
            Bukkit.getOfflinePlayers().filter { it.name?.startsWith(args[0]) ?: false }.forEach {
                list.add(it.name!!)
            }
        }
        return list
    }
}
