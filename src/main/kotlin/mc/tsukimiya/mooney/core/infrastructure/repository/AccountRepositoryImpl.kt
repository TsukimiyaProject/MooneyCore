package mc.tsukimiya.mooney.core.infrastructure.repository

import mc.tsukimiya.mooney.core.domain.Account
import mc.tsukimiya.mooney.core.domain.AccountRepository
import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.domain.Name
import java.util.*
import mc.tsukimiya.mooney.core.infrastructure.dao.Account as AccountDao

class AccountRepositoryImpl : AccountRepository {
    override fun find(owner: UUID): Account? {
        val account = AccountDao.findById(owner) ?: return null
        return Account(owner, Name(account.name), Money(account.money))
    }

    override fun findByName(name: Name): Account? {
        TODO("Not yet implemented")
    }

    override fun findAll(): Map<UUID, Account> {
        val accounts = mutableMapOf<UUID, Account>()
        AccountDao.all().forEach {
            accounts[it.id.value] = Account(it.id.value, Name(it.name), Money(it.money))
        }

        return accounts
    }

    override fun count(): Long {
        return AccountDao.count()
    }

    override fun store(account: Account) {
        val accountDao = AccountDao.findById(account.id)
        if (accountDao != null) {
            accountDao.name = account.name.value
            accountDao.money = account.money.amount
        } else {
            AccountDao.new(account.id) {
                this.name = account.name.value
                this.money = account.money.amount
            }
        }
    }

    override fun delete(owner: UUID) {
        val account = AccountDao.findById(owner)
        account?.delete()
    }
}

fun AccountRepository.Companion.newInstance(): AccountRepository = AccountRepositoryImpl()
