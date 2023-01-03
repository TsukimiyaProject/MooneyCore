package mc.tsukimiya.mooney.core

import mc.tsukimiya.lib4b.command.CommandRegistrar
import mc.tsukimiya.lib4b.db.DatabaseConnector
import mc.tsukimiya.lib4b.lang.MessageFormatter
import mc.tsukimiya.mooney.core.command.MoneyCommand
import mc.tsukimiya.mooney.core.command.MoneySetCommand
import mc.tsukimiya.mooney.core.event.DecreasedMoneyEvent
import mc.tsukimiya.mooney.core.event.IncreasedMoneyEvent
import mc.tsukimiya.mooney.core.event.PaidMoneyEvent
import mc.tsukimiya.mooney.core.event.SetMoneyEvent
import mc.tsukimiya.mooney.core.infrastructure.table.Wallets
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

    override fun onLoad() {
        instance = this
    }

    override fun onEnable() {
        dataFolder.mkdir()
        saveDefaultConfig()

        server.pluginManager.registerEvents(this, this)

        formatter = MessageFormatter(config)
        registerCommands()
        connectDB()
    }

    private fun registerCommands() {
        val registrar = CommandRegistrar(this)
        registrar.registerCommand(MoneyCommand(), MoneySetCommand())
    }

    private fun connectDB() {
        DatabaseConnector.connect(config)
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
        return RetrieveMoneyUseCase().execute(player)
    }

    /**
     * プレイヤーの所持金を設定する
     *
     * @param player
     * @param amount
     */
    fun setMoney(player: UUID, amount: Int) {
        StoreMoneyUseCase().execute(player, amount)
        Bukkit.getPluginManager().callEvent(SetMoneyEvent(player, amount))
    }

    /**
     * プレイヤーの所持金を増やす
     *
     * @param player
     * @param amount
     */
    fun increaseMoney(player: UUID, amount: Int) {
        IncreaseMoneyUseCase().execute(player, amount)
        Bukkit.getPluginManager().callEvent(IncreasedMoneyEvent(player, amount))
    }

    /**
     * プレイヤーの所持金を減らす
     *
     * @param player
     * @param amount
     */
    fun decreaseMoney(player: UUID, amount: Int) {
        DecreaseMoneyUseCase().execute(player, amount)
        Bukkit.getPluginManager().callEvent(DecreasedMoneyEvent(player, amount))
    }

    /**
     * プレイヤーからプレイヤーへお金を払う
     *
     * @param from 支払元
     * @param to   支払先
     * @param amount
     */
    fun payMoney(from: UUID, to: UUID, amount: Int) {
        PayPlayerUseCase().execute(from, to, amount)
        Bukkit.getPluginManager().callEvent(PaidMoneyEvent(from, to, amount))
    }

    /**
     * プレイヤーのデータ作成
     *
     * @param player
     * @param defaultMoney
     */
    fun createAccount(player: UUID, defaultMoney: Int) {
        CreateWalletUseCase().execute(player, defaultMoney)
    }

    /**
     * プレイヤーのデータ削除
     *
     * @param player
     */
    fun deleteAccount(player: UUID) {
        DeleteWalletUseCase().execute(player)
    }
}
