package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.exception.NegativeMoneyException
import mc.tsukimiya.mooney.core.exception.WalletNotFoundException
import mc.tsukimiya.mooney.core.infrastructure.repository.WalletRepositoryImpl
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class IncreaseMoneyUseCase {
    private val repository = WalletRepositoryImpl()

    fun execute(id: UUID, amount: Int) {
        transaction {
            val wallet = repository.find(id) ?: throw WalletNotFoundException(id)
            try {
                repository.save(id, Money(wallet.amount + amount))
            } catch (e: IllegalArgumentException) {
                throw NegativeMoneyException(id)
            }
        }
    }
}
