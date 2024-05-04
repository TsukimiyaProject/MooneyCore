package mc.tsukimiya.mooney.core.infrastructure.repository

import mc.tsukimiya.mooney.core.domain.*
import mc.tsukimiya.mooney.core.infrastructure.dao.Accounts
import org.jetbrains.exposed.sql.lowerCase
import java.time.LocalDateTime
import java.util.*
import mc.tsukimiya.mooney.core.infrastructure.dao.Account as AccountDAO

/**
 * アカウントリポジトリ実装クラス
 *
 * @constructor Create empty Account repository impl
 */
internal class AccountRepositoryImpl : AccountRepository {
    override fun find(id: UUID): Account? {
        val entity = AccountDAO.findById(id) ?: return null
        return Account(entity.id.value, Name(entity.name), Money(entity.money))
    }

    override fun findByName(name: Name): Account? {
        val entity = AccountDAO.find { Accounts.name.lowerCase() eq name.value.lowercase() }.firstOrNull() ?: return null
        return Account(entity.id.value, Name(entity.name), Money(entity.money))
    }

    override fun store(account: Account) {
        val entity = AccountDAO.findById(account.id)
        val date = LocalDateTime.now()

        if (entity != null) {
            entity.money = account.money.amount
            entity.updatedAt = date
        } else {
            AccountDAO.new(account.id) {
                name = account.name.value
                money = account.money.amount
                createdAt = date
                updatedAt = date
            }
        }
    }
}
