package mc.tsukimiya.mooney.core.domain.service

import mc.tsukimiya.mooney.core.domain.model.Money
import mc.tsukimiya.mooney.core.domain.model.MoneyTransaction
import mc.tsukimiya.mooney.core.domain.model.Reason
import mc.tsukimiya.mooney.core.domain.model.Wallet

object TransactionService {
    /**
     * 支払い
     *
     * @param from
     * @param to
     * @param amount
     * @param reason
     * @return
     */
    fun pay(from: Wallet, to: Wallet, amount: Money, reason: Reason? = null): MoneyTransaction? {
        if (!from.canPay(amount) || !to.canCredited(amount)) return null
        from.pay(amount)
        from.credited(amount)
        return MoneyTransaction.NewMoneyTransaction(from, to, amount, reason)
    }
}
