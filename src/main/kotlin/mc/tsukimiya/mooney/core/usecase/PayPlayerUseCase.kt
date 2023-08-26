package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.AccountRepository
import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.domain.MoneyService
import mc.tsukimiya.mooney.core.exception.AccountNotFoundException
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class PayPlayerUseCase(private val repository: AccountRepository) {
    fun execute(from: UUID, to: UUID, amount: Int) {
        transaction {
            val fromAccount = repository.find(from) ?: throw AccountNotFoundException(from)
            val toAccount = repository.find(to) ?: throw AccountNotFoundException(to)
            MoneyService().pay(fromAccount, toAccount, Money(amount))
            repository.store(fromAccount)
            repository.store(toAccount)
        }
    }
}
