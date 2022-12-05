package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.infrastructure.repository.WalletRepositoryImpl
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class DeleteWalletUseCase {
    private val repository = WalletRepositoryImpl()

    fun execute(id: UUID) {
        transaction {
            repository.delete(id)
        }
    }
}
