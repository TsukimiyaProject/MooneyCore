package mc.tsukimiya.mooney.core.presentation.listener

import mc.tsukimiya.mooney.core.MooneyCore
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

internal class PlayerJoinListener(private val defaultMoney: Long, private val reason: String) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        // アカウントが存在しない場合は作成、存在する場合は名前のみ更新
        if (!MooneyCore.api.existsAccount(event.player.uniqueId)) {
            MooneyCore.api.storeAccount(event.player.uniqueId, event.player.name, defaultMoney, reason)
        } else {
            MooneyCore.api.storeAccount(event.player.uniqueId, event.player.name)
        }
    }
}
