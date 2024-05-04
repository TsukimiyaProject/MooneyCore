package mc.tsukimiya.mooney.core.presentation.api

import mc.tsukimiya.mooney.core.MooneyCoreAPI
import mc.tsukimiya.mooney.core.domain.AccountRepository
import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.domain.TransactionHistoryRepository
import mc.tsukimiya.mooney.core.domain.TransactionReason
import mc.tsukimiya.mooney.core.usecase.*
import java.util.*

internal class MooneyCoreAPIImpl(
    private val accountRepository: AccountRepository,
    private val historyRepository: TransactionHistoryRepository,
) : MooneyCoreAPI {
    override fun getID(name: String): UUID? {
        return FindAccountUseCase(accountRepository).execute(name)?.id
    }

    override fun getName(id: UUID): String? {
        return FindAccountUseCase(accountRepository).execute(id)?.name
    }

    override fun getMoney(id: UUID): Long? {
        return FindAccountUseCase(accountRepository).execute(id)?.money
    }

    override fun depositMoney(id: UUID, money: Long, reason: String) {
        require(validateMoney(money))
        require(validateReason(reason))

        DepositMoneyUseCase(accountRepository, historyRepository).execute(id, money, reason)
    }

    override fun withdrawMoney(id: UUID, money: Long, reason: String) {
        require(validateMoney(money))
        require(validateReason(reason))

        WithdrawMoneyUseCase(accountRepository, historyRepository).execute(id, money, reason)
    }

    override fun payMoney(from: UUID, to: UUID, money: Long, fromReason: String, toReason: String) {
        require(validateMoney(money))
        require(validateReason(fromReason))
        require(validateReason(toReason))

        PayMoneyUseCase(accountRepository, historyRepository).execute(from, to, money, fromReason, toReason)
    }

    override fun existsAccount(id: UUID): Boolean {
        return FindAccountUseCase(accountRepository).execute(id) != null
    }

    override fun storeAccount(id: UUID, name: String?, money: Long?, reason: String) {
        StoreAccountUseCase(accountRepository, historyRepository).execute(id, name, money, reason)
    }

    override fun validateMoney(money: Long): Boolean {
        return money in Money.MIN_VALUE..Money.MAX_VALUE
    }

    override fun validateMoney(money: String): Boolean {
        runCatching {
            validateMoney(money.toLong())
        }
            .onSuccess { return it }
            .onFailure { return false }
        return false
    }

    override fun validateReason(reason: String): Boolean {
        return reason.length <= TransactionReason.MAX_LENGTH
    }
}
