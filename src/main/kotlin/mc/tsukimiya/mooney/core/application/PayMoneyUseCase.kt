package mc.tsukimiya.mooney.core.application

import mc.tsukimiya.dbconnector.Database
import mc.tsukimiya.mooney.core.domain.model.Money
import mc.tsukimiya.mooney.core.domain.model.Reason
import mc.tsukimiya.mooney.core.domain.repository.MoneyTransactionHistoryRepository
import mc.tsukimiya.mooney.core.domain.repository.WalletRepository
import mc.tsukimiya.mooney.core.domain.service.TransactionService
import java.util.*

class PayMoneyUseCase(
    private val walletRepository: WalletRepository,
    private val historyRepository: MoneyTransactionHistoryRepository
) {
    fun execute(from: UUID, to: UUID, amount: Int, reason: String? = null): Pair<Int, Int>? {
        return Database.transaction {
            val fromWallet = walletRepository.findByOwner(from) ?: return@transaction null
            val toWallet = walletRepository.findByOwner(to) ?: return@transaction null

            val moneyTransaction = TransactionService.pay(
                fromWallet,
                toWallet,
                Money(amount),
                if (reason != null) Reason(reason) else null
            ) ?: return@transaction null

            walletRepository.store(fromWallet)
            walletRepository.store(toWallet)
            historyRepository.store(moneyTransaction)

            return@transaction Pair(fromWallet.money.value, toWallet.money.value)
        }
    }
}
