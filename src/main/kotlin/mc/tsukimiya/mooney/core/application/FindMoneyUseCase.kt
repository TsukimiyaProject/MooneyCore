package mc.tsukimiya.mooney.core.application

import mc.tsukimiya.dbconnector.Database
import mc.tsukimiya.mooney.core.domain.repository.WalletRepository
import java.util.*

class FindMoneyUseCase(
    private val walletRepository: WalletRepository
) {
    fun execute(player: UUID): Int? {
        return Database.transaction {
            val wallet = walletRepository.findByOwner(player) ?: return@transaction null
            return@transaction wallet.money.value
        }
    }
}
