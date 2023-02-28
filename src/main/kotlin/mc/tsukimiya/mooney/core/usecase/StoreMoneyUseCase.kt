package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.domain.Wallet
import mc.tsukimiya.mooney.core.domain.WalletRepository
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class StoreMoneyUseCase(private val repository: WalletRepository) {
    fun execute(id: UUID, amount: Int) {
        transaction {
            repository.store(Wallet(id, Money(amount)))
        }
    }
}
