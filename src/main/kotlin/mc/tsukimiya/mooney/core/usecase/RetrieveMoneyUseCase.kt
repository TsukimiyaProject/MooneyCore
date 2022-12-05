package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.exception.WalletNotFoundException
import mc.tsukimiya.mooney.core.infrastructure.repository.WalletRepositoryImpl
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class RetrieveMoneyUseCase {
    private val repository = WalletRepositoryImpl()

    fun execute(id: UUID): Int {
        return transaction {
            val wallet = repository.find(id) ?: throw WalletNotFoundException(id)
            wallet.amount
        }
    }
}
