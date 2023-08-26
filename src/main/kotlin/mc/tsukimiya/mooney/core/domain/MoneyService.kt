package mc.tsukimiya.mooney.core.domain

/**
 * お金の支払いを実現するサービスクラス
 */
class MoneyService {
    /**
     * 支払う
     *
     * @param from  支払いする人
     * @param to    支払いされる人
     * @param money 支払いする金額
     */
    fun pay(from: Account, to: Account, money: Money) {
        from.decreaseMoney(money)
        to.increaseMoney(money)
    }
}
