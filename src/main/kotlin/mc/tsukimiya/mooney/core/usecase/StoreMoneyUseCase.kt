package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.exception.WalletNotFoundException
import mc.tsukimiya.mooney.core.infrastructure.repository.WalletRepositoryImpl
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class StoreMoneyUseCase {
    private val repository = WalletRepositoryImpl()

    fun execute(id: UUID, amount: Int) {
        require(amount >= 0) { "Amount must be non-negative, was $amount" }

        transaction {
            repository.find(id) ?: throw WalletNotFoundException(id)
            repository.save(id, Money(amount))
        }
    }
}
