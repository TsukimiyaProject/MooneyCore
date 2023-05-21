package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.domain.AccountRepository
import mc.tsukimiya.mooney.core.exception.WalletNotFoundException
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class IncreaseMoneyUseCase(private val repository: AccountRepository) {
    fun execute(id: UUID, amount: Int) {
        transaction {
            val wallet = repository.find(id) ?: throw WalletNotFoundException(id)

            wallet.increaseMoney(Money(amount))
            repository.store(wallet)
        }
    }
}
