package mc.tsukimiya.mooney.core

import mc.tsukimiya.lib4b.command.CommandRegistrar
import mc.tsukimiya.lib4b.lang.MessageFormatter
import mc.tsukimiya.mooney.core.command.*
import mc.tsukimiya.mooney.core.config.DatabaseConnector
import mc.tsukimiya.mooney.core.domain.WalletRepository
import mc.tsukimiya.mooney.core.event.*
import mc.tsukimiya.mooney.core.infrastructure.dao.Wallets
import mc.tsukimiya.mooney.core.infrastructure.repository.newInstance
import mc.tsukimiya.mooney.core.usecase.*
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class MooneyCore : JavaPlugin(), Listener {
    companion object {
        lateinit var instance: MooneyCore
    }

    lateinit var formatter: MessageFormatter
    private val walletRepository = WalletRepository.newInstance()

    override fun onLoad() {
        instance = this
    }

    override fun onEnable() {
        dataFolder.mkdir()
        saveDefaultConfig()

        server.pluginManager.registerEvents(this, this)

        formatter = MessageFormatter(config)
        registerCommands()
        connect()
    }

    private fun registerCommands() {
        val registrar = CommandRegistrar(this)
        registrar.registerCommand(
            MoneyCommand(),
            MoneySetCommand(),
            MoneyShowCommand(),
            MoneyPlusCommand(),
            MoneyMinusCommand(),
            MoneyPayCommand()
        )
    }

    private fun connect() {
        DatabaseConnector().connect(config)
        transaction {
            SchemaUtils.create(Wallets)
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        createAccount(event.player.uniqueId, config.getInt("default-money"))
    }

    /**
     * 現在のプレイヤーの所持金を取得する
     *
     * @param player
     * @return
     */
    fun getMoney(player: UUID): Int {
        return RetrieveMoneyUseCase(walletRepository).execute(player)
    }

    /**
     * プレイヤーの所持金を設定する
     *
     * @param player
     * @param amount
     */
    fun setMoney(player: UUID, amount: Int) {
        require(amount >= 0) { "Amount must be non-negative was $amount" }

        StoreMoneyUseCase(walletRepository).execute(player, amount)
        Bukkit.getPluginManager().callEvent(MoneyAmountChangedEvent(player))
    }

    /**
     * プレイヤーの所持金を増やす
     *
     * @param player
     * @param amount
     */
    fun increaseMoney(player: UUID, amount: Int) {
        require(amount >= 0) { "Amount must be non-negative was $amount" }

        IncreaseMoneyUseCase(walletRepository).execute(player, amount)
        Bukkit.getPluginManager().callEvent(MoneyAmountChangedEvent(player))
    }

    /**
     * プレイヤーの所持金を減らす
     *
     * @param player
     * @param amount
     */
    fun decreaseMoney(player: UUID, amount: Int) {
        require(amount >= 0) { "Amount must be non-negative was $amount" }

        DecreaseMoneyUseCase(walletRepository).execute(player, amount)
        Bukkit.getPluginManager().callEvent(MoneyAmountChangedEvent(player))
    }

    /**
     * プレイヤーからプレイヤーへお金を払う
     *
     * @param from 支払元
     * @param to   支払先
     * @param amount
     */
    fun payMoney(from: UUID, to: UUID, amount: Int) {
        require(amount >= 0) { "Amount must be non-negative was $amount" }

        PayPlayerUseCase(walletRepository).execute(from, to, amount)
        Bukkit.getPluginManager().callEvent(MoneyAmountChangedEvent(from))
        Bukkit.getPluginManager().callEvent(MoneyAmountChangedEvent(to))
    }

    /**
     * プレイヤーのデータ作成
     *
     * @param player
     * @param defaultMoney
     */
    fun createAccount(player: UUID, defaultMoney: Int) {
        require(defaultMoney >= 0) { "Amount must be non-negative was $defaultMoney" }

        CreateWalletUseCase(walletRepository).execute(player, defaultMoney)
        Bukkit.getPluginManager().callEvent(CreateWalletEvent(player))
    }

    /**
     * プレイヤーのデータ削除
     *
     * @param player
     */
    fun deleteAccount(player: UUID) {
        DeleteWalletUseCase(walletRepository).execute(player)
    }
}
