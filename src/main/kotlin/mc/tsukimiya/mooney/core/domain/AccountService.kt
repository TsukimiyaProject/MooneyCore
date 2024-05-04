package mc.tsukimiya.mooney.core.domain

/**
 * お金の支払いを実現するサービスクラス
 */
class AccountService {
    /**
     * お金の取引
     *
     * @param from  支払
     * @param to    受取
     * @param money 金額
     */
    fun makeMoneyTransaction(from: Account, to: Account, money: Money) {
        val transaction = from.payMoney(money)
        to.receiveMoney(transaction)
    }
}
