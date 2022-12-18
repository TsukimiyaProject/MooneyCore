package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.infrastructure.repository.WalletRepositoryImpl
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class CreateWalletUseCase {
    private val repository = WalletRepositoryImpl()

    fun execute(id: UUID, defaultMoney: Int) {
        require(defaultMoney >= 0) { "Amount must be non-negative was $defaultMoney" }

        transaction {
            if (repository.find(id) == null) {
                repository.create(id, Money(defaultMoney))
            }
        }
    }
}
