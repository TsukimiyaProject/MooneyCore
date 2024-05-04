package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

/**
 * アカウントを保存
 *
 * @property accountRepository
 * @property historyRepository
 * @constructor Create empty Store wallet use case
 */
internal class StoreAccountUseCase(
    private val accountRepository: AccountRepository,
    private val historyRepository: TransactionHistoryRepository,
) {
    fun execute(id: UUID, name: String? = null, money: Long? = null, reason: String = "") {
        transaction {
            var account = accountRepository.find(id)

            var oldMoney = Money(0)
            var history: TransactionHistory? = null

            // アカウントがある場合はUPDATE、ない場合はINSERT
            if (account != null) {
                // 名前が設定されている かつ 名前に変更がある場合は名前を更新
                if (name != null && account.name != Name(name)) account.name = Name(name)

                // 金額が設定されている かつ 金額に変更がある場合
                if (money != null && account.money != Money(money)) {
                    oldMoney = account.money
                    account.money = Money(money)
                    history = TransactionHistoryService().newTransactionHistory(account, oldMoney, TransactionReason(reason))
                }
            } else {
                require(name != null)
                require(money != null)
                account = Account(id, Name(name), Money(money))
                history = TransactionHistoryService().newTransactionHistory(account, oldMoney, TransactionReason(reason))
            }

            accountRepository.store(account)
            if (history != null) historyRepository.store(history)
        }
    }
}
