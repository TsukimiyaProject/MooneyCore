package mc.tsukimiya.mooney.core.domain

import java.util.*

/**
 * アカウントクラス
 *
 * @property owner UUID
 * @property name  マイクラのID
 * @property money 所持金
 */
class Account(val owner: UUID, var name: MinecraftId, var money: Money) {
    /**
     * 所持金をmoneyの量だけ増やす
     *
     * @param money
     */
    fun increaseMoney(money: Money) {
        this.money = Money(this.money.amount + money.amount)
    }

    /**
     * 所持金をmoneyの量だけ減らす
     *
     * @param money
     */
    fun decreaseMoney(money: Money) {
        this.money = Money(this.money.amount - money.amount)
    }
}
