package mc.tsukimiya.mooney.core.domain

class MoneyAmountChangeService {
    fun increase(money: Money, amount: Int): Money {
        return Money(money.amount + amount)
    }

    fun decrease(money: Money, amount: Int): Money {
        return Money(money.amount - amount)
    }

    fun pay(from: Money, to: Money, amount: Int): Pair<Money, Money> {
        return Pair(decrease(from, amount), increase(to, amount))
    }
}
