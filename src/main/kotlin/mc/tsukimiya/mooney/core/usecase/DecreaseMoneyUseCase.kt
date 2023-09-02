package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.AccountRepository
import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.exception.AccountNotFoundException
import mc.tsukimiya.mooney.core.usecase.dto.AccountDto
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class DecreaseMoneyUseCase(private val repository: AccountRepository) {
    fun execute(id: UUID, amount: ULong): AccountDto {
        return transaction {
            val account = repository.find(id) ?: throw AccountNotFoundException(id)
            account.decreaseMoney(Money(amount))
            repository.store(account)

            AccountDto.fromAccount(account)
        }
    }
}
