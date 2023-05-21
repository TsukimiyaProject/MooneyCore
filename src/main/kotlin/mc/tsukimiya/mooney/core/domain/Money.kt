package mc.tsukimiya.mooney.core.domain

import mc.tsukimiya.mooney.core.exception.InvalidMoneyAmountException

/**
 * お金クラス
 *
 * @property amount
 */
data class Money(val amount: Int) {
    init {
        // amountが0未満なら例外投げる
        // システムに0未満の値を存在させないため
        if (amount < 0) {
            throw InvalidMoneyAmountException("Wrong amount : $amount | Require $amount >= 0")
        }
    }
}
