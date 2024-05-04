package mc.tsukimiya.mooney.core.domain

import mc.tsukimiya.mooney.core.exception.MoneyOverLowerLimitException
import mc.tsukimiya.mooney.core.exception.MoneyOverUpperLimitException

/**
 * お金クラス
 */
data class Money(val amount: Long) {
    companion object {
        const val MIN_VALUE = 0L
        const val MAX_VALUE = 9999999999999999
    }

    init {
        // amountが0未満なら例外投げる
        // システムに0未満の値を存在させないため
        if (amount < MIN_VALUE) throw MoneyOverLowerLimitException()
        if (amount > MAX_VALUE) throw MoneyOverUpperLimitException()
    }
}
