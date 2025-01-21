package mc.tsukimiya.mooney.core

import mc.tsukimiya.mooney.core.infrastructure.ConsistentWalletRepositoryImpl
import mc.tsukimiya.mooney.core.infrastructure.MoneyTransactionHistoryRepositoryImpl
import mc.tsukimiya.mooney.core.infrastructure.SimpleWalletRepositoryImpl
import net.milkbowl.vault.economy.Economy
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin

class MooneyCore : JavaPlugin() {
    lateinit var vault: VaultProvider
        private set
    lateinit var messages: FileConfiguration
        private set

    override fun onEnable() {
        saveDefaultConfig()

        val repository = when (config.getString("repo")) {
            "history" -> ConsistentWalletRepositoryImpl()
            else -> SimpleWalletRepositoryImpl()
        }
        vault = VaultProvider(this, repository, MoneyTransactionHistoryRepositoryImpl())
        messages = YamlConfiguration()
        messages.setDefaults(YamlConfiguration.loadConfiguration(getResource("messages.yml")!!.reader()))

        server.servicesManager.register(Economy::class.java, vault, this, ServicePriority.Normal)
    }
}
