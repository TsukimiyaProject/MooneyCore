package mc.tsukimiya.mooney.core.domain

class MoneyAmountChanger {
    fun plus(money: Money, amount: Int): Money {
        return Money(money.amount + amount)
    }

    fun minus(money: Money, amount: Int): Money {
        return Money(money.amount - amount)
    }

    fun pay(from: Money, to: Money, amount: Int): Pair<Money, Money> {
        return Pair(minus(from, amount), plus(to, amount))
    }
}
