package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.WalletRepository
import mc.tsukimiya.mooney.core.exception.WalletNotFoundException
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class RetrieveMoneyUseCase(private val repository: WalletRepository) {
    fun execute(id: UUID): Int {
        return transaction {
            val wallet = repository.find(id) ?: throw WalletNotFoundException(id)
            wallet.amount
        }
    }
}
