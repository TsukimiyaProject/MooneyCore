package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.domain.WalletRepository
import mc.tsukimiya.mooney.core.exception.WalletNotFoundException
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class StoreMoneyUseCase(private val repository: WalletRepository) {
    fun execute(id: UUID, amount: Int) {
        transaction {
            repository.find(id) ?: throw WalletNotFoundException(id)
            repository.save(id, Money(amount))
        }
    }
}
