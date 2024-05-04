package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.*
import mc.tsukimiya.mooney.core.exception.AccountNotFoundException
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

/**
 * お金を支払う
 *
 * @property accountRepository
 * @property historyRepository
 * @constructor Create empty Pay money use case
 */
internal class PayMoneyUseCase(
    private val accountRepository: AccountRepository,
    private val historyRepository: TransactionHistoryRepository
) {
    fun execute(from: UUID, to: UUID, money: Long, fromReason: String, toReason: String) {
        transaction {
            val fromAccount = accountRepository.find(from) ?: throw AccountNotFoundException()
            val toAccount = accountRepository.find(to) ?: throw AccountNotFoundException()
            val fromOldMoney = fromAccount.money
            val toOldMoney = toAccount.money
            AccountService().makeMoneyTransaction(fromAccount, toAccount, Money(money))
            val service = TransactionHistoryService()
            val fromHistory = service.newTransactionHistory(fromAccount, fromOldMoney, TransactionReason(fromReason))
            val toHistory = service.newTransactionHistory(toAccount, toOldMoney, TransactionReason(toReason))

            accountRepository.store(fromAccount)
            accountRepository.store(toAccount)
            historyRepository.store(fromHistory)
            historyRepository.store(toHistory)
        }
    }
}
