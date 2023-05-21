package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.domain.AccountRepository
import mc.tsukimiya.mooney.core.exception.AccountNotFoundException
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class IncreaseMoneyUseCase(private val repository: AccountRepository) {
    fun execute(id: UUID, amount: Int) {
        transaction {
            val account = repository.find(id) ?: throw AccountNotFoundException(id)

            account.increaseMoney(Money(amount))
            repository.store(account)
        }
    }
}
