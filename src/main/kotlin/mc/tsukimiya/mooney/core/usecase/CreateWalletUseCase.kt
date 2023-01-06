package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.domain.WalletRepository
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class CreateWalletUseCase(private val repository: WalletRepository) {
    fun execute(id: UUID, defaultMoney: Int) {
        transaction {
            if (repository.find(id) == null) {
                repository.create(id, Money(defaultMoney))
            }
        }
    }
}
