package mc.tsukimiya.mooney.core.application

import mc.tsukimiya.dbconnector.Database
import mc.tsukimiya.mooney.core.domain.model.Money
import mc.tsukimiya.mooney.core.domain.model.Reason
import mc.tsukimiya.mooney.core.domain.repository.MoneyTransactionHistoryRepository
import mc.tsukimiya.mooney.core.domain.repository.WalletRepository
import java.util.*

class TakeMoneyUseCase(
    private val walletRepository: WalletRepository,
    private val historyRepository: MoneyTransactionHistoryRepository
) {
    /**
     * 所持金を減らす
     *
     * @param player
     * @param amount
     * @param reason
     * @return 減少後の所持金
     */
    fun execute(player: UUID, amount: Int, reason: String? = null): Int? {
        return Database.transaction {
            val wallet = walletRepository.findByOwner(player) ?: return@transaction null

            val money = Money(amount)
            // お金を支払えない場合はnullを返す
            if (!wallet.canPay(money)) return@transaction null
            val moneyTransaction = wallet.pay(money, if (reason != null) Reason(reason) else null)

            walletRepository.store(wallet)
            historyRepository.store(moneyTransaction)

            return@transaction wallet.money.value
        }
    }
}
