package mc.tsukimiya.mooney.core.domain.repository

import mc.tsukimiya.mooney.core.domain.model.MoneyTransaction
import mc.tsukimiya.mooney.core.domain.model.Wallet

interface MoneyTransactionHistoryRepository {
    /**
     * 履歴取得
     *
     * @param wallet
     * @param start  何個目の履歴から取得するか
     * @param offset 取得件数
     * @return
     */
    fun find(wallet: Wallet, start: Int? = null, offset: Int? = null): List<MoneyTransaction>

    fun count(id: Int): Int

    fun store(event: MoneyTransaction)
}
