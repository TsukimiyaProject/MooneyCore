package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.domain.Account
import mc.tsukimiya.mooney.core.domain.AccountRepository
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class CreateAccountUseCase(private val repository: AccountRepository) {
    fun execute(id: UUID, defaultMoney: Int) {
        transaction {
            if (!repository.exists(id)) {
                repository.store(Account(id, Money(defaultMoney)))
            }
        }
    }
}
