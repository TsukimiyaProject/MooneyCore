package mc.tsukimiya.mooney.core

import mc.tsukimiya.lib4b.command.CommandRegistrar
import mc.tsukimiya.lib4b.lang.MessageFormatter
import mc.tsukimiya.mooney.core.command.*
import mc.tsukimiya.mooney.core.config.DatabaseConnector
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class MooneyCore : JavaPlugin(), Listener {
    private lateinit var formatter: MessageFormatter

    override fun onEnable() {
        dataFolder.mkdir()
        saveDefaultConfig()
        config.options().copyDefaults(true)
        saveConfig()

        server.pluginManager.registerEvents(this, this)
        formatter = MessageFormatter(config)
        registerCommands()
        DatabaseConnector().connect(config)
    }

    private fun registerCommands() {
        val registrar = CommandRegistrar(this)
        registrar.registerCommand(
            MoneyCommand(formatter),
            MoneySetCommand(formatter),
            MoneyShowCommand(formatter),
            MoneyPlusCommand(formatter),
            MoneyMinusCommand(formatter),
            MoneyPayCommand(formatter),
            MoneyHelpCommand(formatter)
        )
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        MooneyCoreAPI.createAccount(event.player.uniqueId, event.player.name, config.getInt("default-money"))
    }
}
