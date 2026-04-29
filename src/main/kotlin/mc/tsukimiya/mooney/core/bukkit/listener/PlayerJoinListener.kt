package mc.tsukimiya.mooney.core.bukkit.listener

import net.milkbowl.vault.economy.Economy
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(private val economy: Economy) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (!economy.hasAccount(event.player)) {
            economy.createPlayerAccount(event.player)
        }
    }
}
