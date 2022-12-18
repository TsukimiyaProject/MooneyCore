package mc.tsukimiya.mooney.core

import mc.tsukimiya.mooney.core.event.DecreasedMoneyEvent
import mc.tsukimiya.mooney.core.event.IncreasedMoneyEvent
import mc.tsukimiya.mooney.core.event.PaidMoneyEvent
import mc.tsukimiya.mooney.core.event.SetMoneyEvent
import mc.tsukimiya.mooney.core.usecase.*
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class MooneyCore : JavaPlugin(), Listener {
    companion object {
        private lateinit var instance: MooneyCore

        fun getInstance(): MooneyCore = instance
    }

    override fun onLoad() {
        instance = this
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        createAccount(event.player.uniqueId, 1000)
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
