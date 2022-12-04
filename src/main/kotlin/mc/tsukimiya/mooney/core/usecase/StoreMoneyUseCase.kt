package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.infrastructure.repository.WalletRepositoryImpl
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class StoreMoneyUseCase {
    private val repository = WalletRepositoryImpl()

    fun execute(id: UUID, money: Int) {
        transaction {
            repository.save(id, Money(money))
        }
    }
}
