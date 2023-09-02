package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.Account
import mc.tsukimiya.mooney.core.domain.AccountRepository
import mc.tsukimiya.mooney.core.domain.Name
import mc.tsukimiya.mooney.core.domain.Money
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

/**
 * アカウント保存
 *
 * @property repository
 */
class StoreAccountUseCase(private val repository: AccountRepository) {
    /**
     * nameとmoneyはnullでなければ値の変更があると見なし、値の保存を行う
     * 変更があるときのみ、execute(id, name = 〇〇, money = 〇〇)で呼び出す
     *
     * @param id
     * @param name  変更があるときに代入
     * @param money 変更があるときに代入
     */
    fun execute(id: UUID, name: String? = null, money: ULong? = null) {
        transaction {
            // アカウント取得
            var account = repository.find(id)

            if (account != null) {
                // 引数がnullでなければ値の変更があるとみなし、代入を行う
                if (name != null) account.name = Name(name)
                if (money != null) account.money = Money(money)
            } else {
                require(name != null)
                require(money != null)

                account = Account(id, Name(name), Money(money))
            }

            repository.store(account)
        }
    }
}
