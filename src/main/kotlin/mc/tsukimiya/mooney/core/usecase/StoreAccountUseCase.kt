package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.Account
import mc.tsukimiya.mooney.core.domain.AccountRepository
import mc.tsukimiya.mooney.core.domain.MinecraftId
import mc.tsukimiya.mooney.core.domain.Money
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class StoreAccountUseCase(private val repository: AccountRepository) {
    fun execute(id: UUID, name: String, amount: Int) {
        transaction {
            repository.store(Account(id, MinecraftId(name), Money(amount)))
        }
    }
}
