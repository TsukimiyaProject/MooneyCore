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
     * 名前からIDを取得
     *
     * @param name
     * @return アカウントが存在しない場合はnull
     */
    fun getID(name: String): UUID?

    /**
     * IDから名前を取得
     *
     * @param id
     * @return アカウントが存在しない場合はnull
     */
    fun getName(id: UUID): String?

    /**
     * 現在のプレイヤーの所持金を取得する
     *
     * @param id プレイヤーのUUID
     * @return アカウントが存在しない場合はnull
     */
    fun getMoney(id: UUID): Long?

    /**
     * プレイヤーの所持金を増やす
     *
     * @param id プレイヤーのUUID
     * @param money 増やす額
     * @param reason 理由
     */
    fun depositMoney(id: UUID, money: Long, reason: String = "")

    /**
     * プレイヤーの所持金を減らす
     *
     * @param id プレイヤーのUUID
     * @param money 減らす額
     * @param reason 理由
     */
    fun withdrawMoney(id: UUID, money: Long, reason: String = "")

    /**
     * プレイヤーからプレイヤーへお金を支払う
     *
     * @param from 支払元
     * @param to 支払先
     * @param money 支払額
     * @param toReason 理由
     * @param fromReason 理由
     */
    fun payMoney(from: UUID, to: UUID, money: Long, fromReason: String = "", toReason: String = "")

    /**
     * アカウントが存在するか
     *
     * @param id
     * @return
     */
    fun existsAccount(id: UUID): Boolean

    /**
     * アカウント保存
     *
     * @param id プレイヤーのUUID
     * @param name 名前を更新する場合に設定
     * @param money 金額を更新する場合に設定
     * @param reason 金額を更新する場合に設定
     */
    fun storeAccount(id: UUID, name: String? = null, money: Long? = null, reason: String = "")

    /**
     * お金が有効範囲か
     *
     * @param money
     * @return
     */
    fun validateMoney(money: Long): Boolean

    /**
     * お金が有効範囲か
     *
     * @param money
     * @return
     */
    fun validateMoney(money: String): Boolean

    /**
     * 理由が有効文字数内か
     *
     * @param reason
     * @return
     */
    fun validateReason(reason: String): Boolean
}
