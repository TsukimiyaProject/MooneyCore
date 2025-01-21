package mc.tsukimiya.mooney.core.application

import mc.tsukimiya.dbconnector.Database
import mc.tsukimiya.mooney.core.domain.model.Money
import mc.tsukimiya.mooney.core.domain.model.Reason
import mc.tsukimiya.mooney.core.domain.repository.MoneyTransactionHistoryRepository
import mc.tsukimiya.mooney.core.domain.repository.WalletRepository
import java.util.*

class GiveMoneyUseCase(
    private val walletRepository: WalletRepository,
    private val historyRepository: MoneyTransactionHistoryRepository
) {
    /**
     * 所持金を増やす
     *
     * @param player
     * @param amount
     * @param reason
     * @return 増加後の所持金
     */
    fun execute(player: UUID, amount: Int, reason: String? = null): Int? {
        require(amount >= 0)

        return Database.transaction {
            val wallet = walletRepository.findByOwner(player) ?: return@transaction null

            val money = Money(amount)
            if (!wallet.canCredited(money)) return@transaction null
            val moneyTransaction = wallet.credited(Money(amount), if (reason != null) Reason(reason) else null)

            walletRepository.store(wallet)
            historyRepository.store(moneyTransaction)

            return@transaction wallet.money.value
        }
    }
}
