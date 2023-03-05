package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.domain.PayMoneyService
import mc.tsukimiya.mooney.core.domain.WalletRepository
import mc.tsukimiya.mooney.core.domain.exception.WalletNotFoundException
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class PayPlayerUseCase(private val repository: WalletRepository) {
    fun execute(from: UUID, to: UUID, amount: Int) {
        transaction {
            val fromWallet = repository.find(from) ?: throw WalletNotFoundException(from)
            val toWallet = repository.find(to) ?: throw WalletNotFoundException(to)
            PayMoneyService().pay(fromWallet, toWallet, Money(amount))
            repository.store(fromWallet)
            repository.store(toWallet)
        }
    }
}
