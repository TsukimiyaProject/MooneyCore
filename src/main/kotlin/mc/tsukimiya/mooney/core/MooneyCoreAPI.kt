package mc.tsukimiya.mooney.core

import mc.tsukimiya.mooney.core.domain.AccountRepository
import mc.tsukimiya.mooney.core.event.CreateWalletEvent
import mc.tsukimiya.mooney.core.event.MoneyAmountChangedEvent
import mc.tsukimiya.mooney.core.infrastructure.repository.newInstance
import mc.tsukimiya.mooney.core.usecase.*
import org.bukkit.Bukkit
import java.util.*

object MooneyCoreAPI {
    private val accountRepository = AccountRepository.newInstance()

    /**
     * 現在のプレイヤーの所持金を取得する
     *
     * @param player
     * @return
     */
    fun getMoney(player: UUID): Int {
        return FetchAccountUseCase(accountRepository).execute(player).money
    }

    /**
     * プレイヤーの所持金を設定する
     *
     * @param uuid
     * @param amount
     */
    fun setMoney(uuid: UUID, amount: Int) {
        require(amount >= 0) { "Amount must be non-negative was $amount" }

        StoreAccountUseCase(accountRepository).execute(uuid, money = amount)
        Bukkit.getPluginManager().callEvent(MoneyAmountChangedEvent(uuid))
    }

    /**
     * プレイヤーの所持金を増やす
     *
     * @param uuid
     * @param amount
     */
    fun increaseMoney(uuid: UUID, amount: Int) {
        require(amount >= 0) { "Amount must be non-negative was $amount" }

        IncreaseMoneyUseCase(accountRepository).execute(uuid, amount)
        Bukkit.getPluginManager().callEvent(MoneyAmountChangedEvent(uuid))
    }

    /**
     * プレイヤーの所持金を減らす
     *
     * @param uuid
     * @param amount
     */
    fun decreaseMoney(uuid: UUID, amount: Int) {
        require(amount >= 0) { "Amount must be non-negative was $amount" }

        DecreaseMoneyUseCase(accountRepository).execute(uuid, amount)
        Bukkit.getPluginManager().callEvent(MoneyAmountChangedEvent(uuid))
    }

    /**
     * プレイヤーからプレイヤーへお金を払う
     *
     * @param from 支払元
     * @param to   支払先
     * @param amount
     */
    fun payMoney(from: UUID, to: UUID, amount: Int) {
        require(amount >= 0) { "Amount must be non-negative was $amount" }

        PayPlayerUseCase(accountRepository).execute(from, to, amount)
        Bukkit.getPluginManager().callEvent(MoneyAmountChangedEvent(from))
        Bukkit.getPluginManager().callEvent(MoneyAmountChangedEvent(to))
    }

    /**
     * プレイヤーのデータ作成
     *
     * @param player
     * @param defaultMoney
     */
    fun createAccount(uuid: UUID, name: String, defaultMoney: Int) {
        require(defaultMoney >= 0) { "Amount must be non-negative was $defaultMoney" }

        StoreAccountUseCase(accountRepository).execute(uuid, name, defaultMoney)
        Bukkit.getPluginManager().callEvent(CreateWalletEvent(uuid))
    }

    /**
     * プレイヤーのデータ削除
     *
     * @param player
     */
    fun deleteAccount(player: UUID) {
        DeleteAccountUseCase(accountRepository).execute(player)
    }
}
