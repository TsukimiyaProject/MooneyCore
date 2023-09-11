package mc.tsukimiya.mooney.core

import java.util.*

/**
 * APIのインターフェース
 *
 * @author deceitya
 *
 */
interface MooneyCoreAPI {
    /**
     * 現在のプレイヤーの所持金を取得する
     *
     * @param uuid プレイヤーのUUID
     * @return 所持金
     */
    fun getMoney(uuid: UUID): MooneyCoreAPIResult

    /**
     * プレイヤーの所持金を設定する
     *
     * @param uuid プレイヤーのUUID
     * @param amount 設定額
     * @return 設定した後の所持金
     */
    fun setMoney(uuid: UUID, amount: ULong): MooneyCoreAPIResult

    /**
     * プレイヤーの所持金を増やす
     *
     * @param uuid プレイヤーのUUID
     * @param amount 増やす額
     * @return 増やした後の所持金
     */
    fun increaseMoney(uuid: UUID, amount: ULong): MooneyCoreAPIResult

    /**
     * プレイヤーの所持金を減らす
     *
     * @param uuid プレイヤーのUUID
     * @param amount 減らす額
     * @return 減らした後の所持金
     */
    fun decreaseMoney(uuid: UUID, amount: ULong): MooneyCoreAPIResult

    /**
     * プレイヤーからプレイヤーへお金を支払う
     *
     * @param from 支払元
     * @param to 支払先
     * @param amount 支払額
     * @return 支払った後の所持金
     */
    fun payMoney(from: UUID, to: UUID, amount: ULong): Pair<MooneyCoreAPIResult, MooneyCoreAPIResult>

    /**
     * アカウント作成
     *
     * @param uuid
     * @param name
     * @param defaultMoney
     */
    fun createAccount(uuid: UUID, name: String, defaultMoney: ULong = 0u)

    /**
     * アカウント削除
     *
     * @param uuid プレイヤーのUUID
     */
    fun deleteAccount(uuid: UUID)
}
