package mc.tsukimiya.mooney.core.command

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

abstract class BaseSubCommand(
    val name: String,
    val description: String,
    val usage: String,
    val aliases: Array<String> = arrayOf(),
    val permission: String = ""
) : CommandRunnable, RestrictSenderTrait {
    fun testPermission(sender: CommandSender): Boolean {
        if (testPermissionSilent(sender)) {
            return true
        }

        sender.sendMessage(Bukkit.permissionMessage())

        return false
    }

    fun testPermissionSilent(sender: CommandSender): Boolean {
        if (permission.isEmpty()) {
            return true
        }

        permission.split(";").forEach {
            if (sender.hasPermission(it)) {
                return true
            }
        }

        return false
    }
}
