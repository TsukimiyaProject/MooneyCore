package mc.tsukimiya.mooney.core

import mc.tsukimiya.mooney.core.exception.AccountNotFoundException
import mc.tsukimiya.mooney.core.exception.InvalidMoneyAmountException

sealed interface MooneyCoreAPIResult {
    class Success(val money: ULong) : MooneyCoreAPIResult

    class NotFoundAccount(val exception: AccountNotFoundException) : MooneyCoreAPIResult

    class InvalidMoney(val exception: InvalidMoneyAmountException) : MooneyCoreAPIResult
}
