package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.AccountRepository
import mc.tsukimiya.mooney.core.domain.Name
import mc.tsukimiya.mooney.core.usecase.dto.AccountDTO
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

/**
 * アカウントを取得する
 *
 * @property accountRepository
 * @constructor Create empty Find wallet use case
 */
internal class FindAccountUseCase(private val accountRepository: AccountRepository) {
    /**
     * IDから取得
     *
     * @param id
     * @return
     */
    fun execute(id: UUID): AccountDTO? {
        return transaction {
            val account = accountRepository.find(id) ?: return@transaction null

            AccountDTO(account.id, account.name.value, account.money.amount)
        }
    }

    /**
     * 名前から取得
     *
     * @param name
     * @return
     */
    fun execute(name: String): AccountDTO? {
        return transaction {
            val account = accountRepository.findByName(Name(name)) ?: return@transaction null

            AccountDTO(account.id, account.name.value, account.money.amount)
        }
    }
}
