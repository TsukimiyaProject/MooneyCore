package mc.tsukimiya.mooney.core.domain

class PayMoneyService {
    fun pay(from: Wallet, to: Wallet, money: Money) {
        from.decreaseMoney(money)
        to.increaseMoney(money)
    }
}
