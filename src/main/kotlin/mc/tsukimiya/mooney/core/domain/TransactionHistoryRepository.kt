package mc.tsukimiya.mooney.core.domain

import java.util.*

interface TransactionHistoryRepository {
    /**
     * ログのIDから取得
     *
     * @param id
     * @return
     */
    fun find(id: Long): TransactionHistory?

    /**
     * アカウントのUUIDからログを取得
     *
     * @param account
     * @return
     */
    fun findByAccount(account: UUID): List<TransactionHistory>

    /**
     * ログを保存
     *
     * @param history
     */
    fun store(history: TransactionHistory)
}
