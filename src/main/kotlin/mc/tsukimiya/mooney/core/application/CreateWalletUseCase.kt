package mc.tsukimiya.mooney.core.application

import mc.tsukimiya.dbconnector.Database
import mc.tsukimiya.mooney.core.domain.model.Money
import mc.tsukimiya.mooney.core.domain.model.Wallet
import mc.tsukimiya.mooney.core.domain.repository.MoneyTransactionHistoryRepository
import mc.tsukimiya.mooney.core.domain.repository.WalletRepository
import java.util.*

class CreateWalletUseCase(
    private val walletRepository: WalletRepository,
    private val historyRepository: MoneyTransactionHistoryRepository
) {
    fun execute(player: UUID, defaultMoney: Int = 0): Boolean {
        require(defaultMoney >= 0)

        return Database.transaction {
            if (walletRepository.findByOwner(player) != null) return@transaction false

            walletRepository.store(Wallet.NewWallet(player))

            // 初期所持金を与える
            val wallet = walletRepository.findByOwner(player) ?: throw RuntimeException()
            val moneyTransaction = wallet.credited(Money(defaultMoney))

            // 履歴とwallet自体の保存
            walletRepository.store(wallet)
            historyRepository.store(moneyTransaction)

            return@transaction true
        }
    }
}
