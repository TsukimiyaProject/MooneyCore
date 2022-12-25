package mc.tsukimiya.mooney.core.command

import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.RemoteConsoleCommandSender
import org.bukkit.entity.Player

interface RestrictSenderTrait {
    /**
     * プレイヤーからのコマンドを禁止する
     *
     * @return
     */
    fun isRestrictPlayer(): Boolean = false

    /**
     * コンソールからのコマンドを禁止にする
     *
     * @return
     */
    fun isRestrictConsole(): Boolean = false

    /**
     * 制限されているならtrue
     *
     * @return
     */
    fun isRestriction(sender: CommandSender): Boolean {
        when (sender) {
            is ConsoleCommandSender, is RemoteConsoleCommandSender -> {
                if (isRestrictConsole()) {
                    sender.sendMessage("This command must be executed in-game.")
                    return true
                }
                return false
            }
            is Player -> {
                if (isRestrictPlayer()) {
                    sender.sendMessage("This command must be executed from a server console.")
                    return true
                }
                return false
            }
            else -> return false
        }
    }
}
