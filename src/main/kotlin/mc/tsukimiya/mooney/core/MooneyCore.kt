package mc.tsukimiya.mooney.core

import mc.tsukimiya.mooney.core.usecase.*
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class MooneyCore : JavaPlugin() {
    companion object {
        private lateinit var instance: MooneyCore

        fun getInstance(): MooneyCore = instance
    }

    override fun onLoad() {
        instance = this
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
    }

    /**
     * プレイヤーの所持金を増やす
     *
     * @param player
     * @param amount
     */
    fun addMoney(player: UUID, amount: Int) {
        IncreaseMoneyUseCase().execute(player, amount)
    }

    /**
     * プレイヤーの所持金を減らす
     *
     * @param player
     * @param amount
     */
    fun reduceMoney(player: UUID, amount: Int) {
        DecreaseMoneyUseCase().execute(player, amount)
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
    }

    /**
     * プレイヤーのアカウント削除
     *
     * @param player
     */
    fun deleteAccount(player: UUID) {
        DeleteWalletUseCase().execute(player)
    }
}
