package mc.tsukimiya.mooney.core.domain

import java.util.*

class Wallet(val owner: UUID, money: Money) {
    var money: Money = money; private set

    fun increaseMoney(money: Money) {
        this.money = Money(this.money.amount + money.amount)
    }

    fun decreaseMoney(money: Money) {
        this.money = Money(this.money.amount - money.amount)
    }
}
