package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.domain.Wallet
import mc.tsukimiya.mooney.core.domain.WalletRepository
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class CreateWalletUseCase(private val repository: WalletRepository) {
    fun execute(id: UUID, defaultMoney: Int) {
        transaction {
            if (!repository.exists(id)) {
                repository.store(Wallet(id, Money(defaultMoney)))
            }
        }
    }
}
