package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.exception.NegativeMoneyException
import mc.tsukimiya.mooney.core.exception.WalletNotFoundException
import mc.tsukimiya.mooney.core.infrastructure.repository.WalletRepositoryImpl
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class PayPlayerUseCase {
    private val repository = WalletRepositoryImpl()

    fun execute(from: UUID, to: UUID, amount: Int) {
        transaction {
            val fromMoney = repository.find(from) ?: throw WalletNotFoundException(from)
            val toMoney = repository.find(to) ?: throw WalletNotFoundException(to)
            try {
                repository.save(from, Money(fromMoney.amount - amount))
                repository.save(to, Money(toMoney.amount + amount))
            } catch (e: IllegalArgumentException) {
                throw NegativeMoneyException(from)
            }
        }
    }
}
