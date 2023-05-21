package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.AccountRepository
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class DeleteWalletUseCase(private val repository: AccountRepository) {
    fun execute(id: UUID) {
        transaction {
            repository.delete(id)
        }
    }
}
