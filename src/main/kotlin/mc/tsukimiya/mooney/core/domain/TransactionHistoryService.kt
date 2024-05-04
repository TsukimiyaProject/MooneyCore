package mc.tsukimiya.mooney.core.domain

import java.time.LocalDateTime

class TransactionHistoryService {
    /**
     * 新規履歴作成
     *
     * @param account
     * @param oldMoney
     * @param reason
     * @return
     */
    fun newTransactionHistory(
        account: Account,
        oldMoney: Money,
        reason: TransactionReason
    ): TransactionHistory {
        return TransactionHistory(-1, account, oldMoney, account.money, reason, LocalDateTime.now())
    }
}
