package mc.tsukimiya.mooney.core.domain

import java.util.*

/**
 * アカウント
 */
data class Account(val id: UUID, var name: Name, var money: Money) {
    /**
     * お金を受け取る
     *
     * @param money
     */
    fun receiveMoney(money: Money) {
        this.money = Money(this.money.amount + money.amount)
    }

    /**
     * お金を支払う
     *
     * @param money
     * @return
     */
    fun payMoney(money: Money): Money {
        this.money = Money(this.money.amount - money.amount)
        return Money(money.amount)
    }
}
