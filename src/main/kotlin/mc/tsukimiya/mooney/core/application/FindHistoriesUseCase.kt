package mc.tsukimiya.mooney.core.application

import mc.tsukimiya.dbconnector.Database
import mc.tsukimiya.mooney.core.domain.model.MoneyTransaction
import mc.tsukimiya.mooney.core.domain.repository.MoneyTransactionHistoryRepository
import mc.tsukimiya.mooney.core.domain.repository.WalletRepository
import java.util.*

class FindHistoriesUseCase(
    private val walletRepository: WalletRepository,
    private val historyRepository: MoneyTransactionHistoryRepository
) {
    /**
     * ログ検索
     *
     * @param player プレイヤー
     * @param start  何個目の履歴から取得するか
     * @param offset 取得件数
     * @return <履歴, 全件数>
     */
    fun execute(player: UUID, start: Int? = null, offset: Int? = null): Pair<List<MoneyTransaction>, Int> {
        return Database.transaction {
            val wallet = walletRepository.findByOwner(player) ?: return@transaction Pair(listOf<MoneyTransaction>(), 0)
            val moneyTransactions = historyRepository.find(wallet, start, offset)
            val count = historyRepository.count(wallet.id)

            return@transaction Pair(moneyTransactions, count)
        }
    }
}
