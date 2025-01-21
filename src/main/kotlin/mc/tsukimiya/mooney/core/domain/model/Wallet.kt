package mc.tsukimiya.mooney.core.domain.model

import java.util.*

open class Wallet(val id: Int, val owner: UUID, money: Money) {
    var money = money
        private set

    init {
        require(id >= 0)
    }

    /**
     * 支払えるか
     *
     * @param amount
     * @return
     */
    fun canPay(amount: Money): Boolean {
        return Money.validate(money.value - amount.value)
    }

    /**
     * 支払
     *
     * @param amount
     * @param reason
     * @return
     */
    fun pay(amount: Money, reason: Reason? = null): MoneyTransaction {
        money = Money(money.value - amount.value)
        return MoneyTransaction.NewMoneyTransaction(this, null, amount, reason)
    }

    /**
     * 受け取れるか
     *
     * @param amount
     * @return
     */
    fun canCredited(amount: Money): Boolean {
        // お金を最大値までためると取引できなくなるみたいなパターンを防ぐため。
        // 問題としては支払う側は支払って受け取る側は受け取ってないみたいな(要するにお金を捨てた)状態になり、
        // サーバー全体の資産を見たときに資産が単に減ってしまうだけになってしまうが、今回はそんなに厳密にはしないためこれでヨシ
        return true
    }

    /**
     * 受取
     *
     * @param amount
     * @param reason
     * @return
     */
    fun credited(amount: Money, reason: Reason? = null): MoneyTransaction {
        money = runCatching { Money(money.value + amount.value) }.getOrElse { Money(Money.MAX_VALUE) }
        return MoneyTransaction.NewMoneyTransaction(null, this, amount, reason)
    }

    class NewWallet(owner: UUID) : Wallet(0, owner, Money(0))
}
