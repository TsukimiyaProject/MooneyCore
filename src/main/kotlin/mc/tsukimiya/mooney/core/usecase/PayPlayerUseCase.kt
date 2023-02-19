package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.MoneyAmountChangeService
import mc.tsukimiya.mooney.core.domain.WalletRepository
import mc.tsukimiya.mooney.core.exception.InvalidMoneyAmountException
import mc.tsukimiya.mooney.core.exception.WalletNotFoundException
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class PayPlayerUseCase(private val repository: WalletRepository) {
    fun execute(from: UUID, to: UUID, amount: Int) {
        transaction {
            val fromMoney = repository.find(from) ?: throw WalletNotFoundException(from)
            val toMoney = repository.find(to) ?: throw WalletNotFoundException(to)
            try {
                val pair = MoneyAmountChangeService().pay(fromMoney, toMoney, amount)
                repository.save(from, pair.first)
                repository.save(to, pair.second)
            } catch (e: IllegalArgumentException) {
                throw InvalidMoneyAmountException(from)
            }
        }
    }
}
