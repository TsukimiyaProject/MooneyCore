package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.*
import mc.tsukimiya.mooney.core.exception.AccountNotFoundException
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

/**
 * お金を減らす
 *
 * @property accountRepository
 * @property historyRepository
 * @constructor Create empty Decrease money use case
 */
internal class WithdrawMoneyUseCase(
    private val accountRepository: AccountRepository,
    private val historyRepository: TransactionHistoryRepository
) {
    fun execute(id: UUID, money: Long, reason: String) {
        transaction {
            val account = accountRepository.find(id) ?: throw AccountNotFoundException()
            val oldMoney = account.money
            account.payMoney(Money(money))
            val history = TransactionHistoryService().newTransactionHistory(account, oldMoney, TransactionReason(reason))

            accountRepository.store(account)
            historyRepository.store(history)
        }
    }
}
