package mc.tsukimiya.mooney.core.infrastructure.repository

import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.domain.Account
import mc.tsukimiya.mooney.core.domain.AccountRepository
import mc.tsukimiya.mooney.core.domain.MinecraftId
import java.util.*
import mc.tsukimiya.mooney.core.infrastructure.dao.Account as AccountDao

class AccountRepositoryImpl : AccountRepository {
    override fun find(owner: UUID): Account? {
        val account = AccountDao.findById(owner) ?: return null
        return Account(owner, MinecraftId(account.name), Money(account.money))
    }

    override fun findAll(): Map<UUID, Account> {
        val wallets = mutableMapOf<UUID, Account>()
        WalletDao.all().forEach {
            wallets[it.id.value] = Account(it.id.value, Money(it.money))
        }

        return wallets
    }

    override fun count(): Long {
        return WalletDao.count()
    }

    override fun store(account: Account) {
        val walletDao = WalletDao.findById(account.id)
        if (walletDao != null) {
            walletDao.money = account.money.amount
        } else {
            WalletDao.new(account.id) { this.money = account.money.amount }
        }
    }

    override fun delete(owner: UUID) {
        val wallet = WalletDao.findById(owner)
        wallet?.delete()
    }
}

fun AccountRepository.Companion.newInstance(): AccountRepository = WalletRepositoryImpl()
