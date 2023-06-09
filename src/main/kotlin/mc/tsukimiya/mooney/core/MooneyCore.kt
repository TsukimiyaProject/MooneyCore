package mc.tsukimiya.mooney.core

import mc.tsukimiya.lib4b.command.CommandRegistrar
import mc.tsukimiya.lib4b.lang.MessageFormatter
import mc.tsukimiya.mooney.core.command.*
import mc.tsukimiya.mooney.core.config.DatabaseConnector
import mc.tsukimiya.mooney.core.domain.AccountRepository
import mc.tsukimiya.mooney.core.event.CreateWalletEvent
import mc.tsukimiya.mooney.core.event.MoneyAmountChangedEvent
import mc.tsukimiya.mooney.core.infrastructure.repository.newInstance
import mc.tsukimiya.mooney.core.usecase.*
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class MooneyCore : JavaPlugin(), Listener {
    companion object {
        lateinit var instance: MooneyCore
    }

    lateinit var formatter: MessageFormatter
    private val accountRepository = AccountRepository.newInstance()

    override fun onLoad() {
        instance = this
    }

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
            MoneyCommand(),
            MoneySetCommand(),
            MoneyShowCommand(),
            MoneyPlusCommand(),
            MoneyMinusCommand(),
            MoneyPayCommand(),
            MoneyHelpCommand()
        )
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        createAccount(event.player.uniqueId, event.player.name, config.getInt("default-money"))
    }

    /**
     * 現在のプレイヤーの所持金を取得する
     *
     * @param player
     * @return
     */
    fun getMoney(player: UUID): Int {
        return FetchAccountUseCase(accountRepository).execute(player).money
    }

    /**
     * プレイヤーの所持金を設定する
     *
     * @param uuid
     * @param amount
     */
    fun setMoney(uuid: UUID, amount: Int) {
        require(amount >= 0) { "Amount must be non-negative was $amount" }

        StoreAccountUseCase(accountRepository).execute(uuid, money = amount)
        Bukkit.getPluginManager().callEvent(MoneyAmountChangedEvent(uuid))
    }

    /**
     * プレイヤーの所持金を増やす
     *
     * @param uuid
     * @param amount
     */
    fun increaseMoney(uuid: UUID, amount: Int) {
        require(amount >= 0) { "Amount must be non-negative was $amount" }

        IncreaseMoneyUseCase(accountRepository).execute(uuid, amount)
        Bukkit.getPluginManager().callEvent(MoneyAmountChangedEvent(uuid))
    }

    /**
     * プレイヤーの所持金を減らす
     *
     * @param uuid
     * @param amount
     */
    fun decreaseMoney(uuid: UUID, amount: Int) {
        require(amount >= 0) { "Amount must be non-negative was $amount" }

        DecreaseMoneyUseCase(accountRepository).execute(uuid, amount)
        Bukkit.getPluginManager().callEvent(MoneyAmountChangedEvent(uuid))
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

        PayPlayerUseCase(accountRepository).execute(from, to, amount)
        Bukkit.getPluginManager().callEvent(MoneyAmountChangedEvent(from))
        Bukkit.getPluginManager().callEvent(MoneyAmountChangedEvent(to))
    }

    /**
     * プレイヤーのデータ作成
     *
     * @param player
     * @param defaultMoney
     */
    fun createAccount(uuid: UUID, name: String, defaultMoney: Int) {
        require(defaultMoney >= 0) { "Amount must be non-negative was $defaultMoney" }

        StoreAccountUseCase(accountRepository).execute(uuid, name, defaultMoney)
        Bukkit.getPluginManager().callEvent(CreateWalletEvent(uuid))
    }

    /**
     * プレイヤーのデータ削除
     *
     * @param player
     */
    fun deleteAccount(player: UUID) {
        DeleteAccountUseCase(accountRepository).execute(player)
    }
}
