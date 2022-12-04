package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.infrastructure.repository.WalletRepositoryImpl
import java.util.*

class DeleteWalletUseCase {
    private val repository = WalletRepositoryImpl()

    fun execute(id: UUID) {
        repository.delete(id)
    }
}
