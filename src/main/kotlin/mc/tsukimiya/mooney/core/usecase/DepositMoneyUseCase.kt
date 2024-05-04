package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.*
import mc.tsukimiya.mooney.core.exception.AccountNotFoundException
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

/**
 * お金を増やす
 *
 * @property accountRepository
 * @property historyRepository
 * @constructor Create empty Increase money use case
 */
internal class DepositMoneyUseCase(
    private val accountRepository: AccountRepository,
    private val historyRepository: TransactionHistoryRepository
) {
    fun execute(id: UUID, money: Long, reason: String) {
        transaction {
            val account = accountRepository.find(id) ?: throw AccountNotFoundException()
            val oldMoney = account.money
            account.receiveMoney(Money(money))
            val history = TransactionHistoryService().newTransactionHistory(account, oldMoney, TransactionReason(reason))

            accountRepository.store(account)
            historyRepository.store(history)
        }
    }
}
