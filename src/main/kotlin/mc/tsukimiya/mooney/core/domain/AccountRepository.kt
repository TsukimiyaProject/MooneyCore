package mc.tsukimiya.mooney.core.domain

import java.util.*

interface AccountRepository {
    companion object

    /**
     * アカウント取得
     *
     * @param owner
     * @return 見つからなければnullを返す
     */
    fun find(owner: UUID): Account?

    /**
     * マイクラIDからアカウント取得
     *
     * @param name
     * @return 見つからなければnullを返す
     */
    fun findByName(name: MinecraftId): Account?

    /**
     * 全アカウント取得
     *
     * @return
     */
    fun findAll(): Map<UUID, Account>

    /**
     * 登録されているアカウント数
     *
     * @return
     */
    fun count(): Long

    /**
     * アカウントを保存
     *
     * @param wallet
     */
    fun store(account: Account)

    /**
     * アカウントを削除
     *
     * @param owner
     */
    fun delete(owner: UUID)
}
