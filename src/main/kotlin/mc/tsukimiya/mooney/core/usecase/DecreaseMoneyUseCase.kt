package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.MoneyAmountChangeService
import mc.tsukimiya.mooney.core.domain.WalletRepository
import mc.tsukimiya.mooney.core.exception.InvalidMoneyAmountException
import mc.tsukimiya.mooney.core.exception.WalletNotFoundException
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class DecreaseMoneyUseCase(private val repository: WalletRepository) {
    fun execute(id: UUID, amount: Int) {
        transaction {
            val money = repository.find(id) ?: throw WalletNotFoundException(id)
            try {
                repository.save(id, MoneyAmountChangeService().decrease(money, amount))
            } catch (e: IllegalArgumentException) {
                throw InvalidMoneyAmountException(id)
            }
        }
    }
}
