package mc.tsukimiya.mooney.core.domain

import mc.tsukimiya.mooney.core.domain.exception.InvalidMoneyAmountException

data class Money(val amount: Int) {
    init {
        if (amount < 0) {
            throw InvalidMoneyAmountException(amount)
        }
    }
}
