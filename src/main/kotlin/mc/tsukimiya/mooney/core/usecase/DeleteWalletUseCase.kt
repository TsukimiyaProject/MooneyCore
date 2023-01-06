package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.WalletRepository
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class DeleteWalletUseCase(private val repository: WalletRepository) {
    fun execute(id: UUID) {
        transaction {
            repository.delete(id)
        }
    }
}
