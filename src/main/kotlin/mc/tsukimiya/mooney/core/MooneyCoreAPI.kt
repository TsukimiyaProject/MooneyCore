package mc.tsukimiya.mooney.core

import java.util.UUID

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
     * @return 所持金 アカウントが存在しない場合はnull
     */
    fun getMoney(uuid: UUID): ULong?

    /**
     * プレイヤーの所持金を設定する
     *
     * @param uuid プレイヤーのUUID
     * @param amount 設定額
     */
    fun setMoney(uuid: UUID, amount: ULong): Boolean

    /**
     * プレイヤーの所持金を増やす
     *
     * @param uuid プレイヤーのUUID
     * @param amount 増やす額
     * @return 成功したか
     */
    fun increaseMoney(uuid: UUID, amount: ULong): Boolean

    /**
     * プレイヤーの所持金を減らす
     *
     * @param uuid プレイヤーのUUID
     * @param amount 減らす額
     * @return 成功したか
     */
    fun decreaseMoney(uuid: UUID, amount: ULong): Boolean

    /**
     * プレイヤーからプレイヤーへお金を支払う
     *
     * @param from 支払元
     * @param to 支払先
     * @param amount 支払額
     * @return 成功したか
     */
    fun payMoney(from: UUID, to: UUID, amount: ULong): Boolean

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
