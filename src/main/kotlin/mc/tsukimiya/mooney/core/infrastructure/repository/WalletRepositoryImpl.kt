package mc.tsukimiya.mooney.core.infrastructure.repository

import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.domain.Account
import mc.tsukimiya.mooney.core.domain.AccountRepository
import java.util.*
import mc.tsukimiya.mooney.core.infrastructure.dao.Wallet as WalletDao

class WalletRepositoryImpl : AccountRepository {
    override fun exists(owner: UUID): Boolean {
        return WalletDao.findById(owner) != null
    }

    override fun find(owner: UUID): Account? {
        val wallet = WalletDao.findById(owner) ?: return null
        return Account(owner, Money(wallet.money))
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

    override fun store(wallet: Account) {
        val walletDao = WalletDao.findById(wallet.owner)
        if (walletDao != null) {
            walletDao.money = wallet.money.amount
        } else {
            WalletDao.new(wallet.owner) { this.money = wallet.money.amount }
        }
    }

    override fun delete(owner: UUID) {
        val wallet = WalletDao.findById(owner)
        wallet?.delete()
    }
}

fun AccountRepository.Companion.newInstance(): AccountRepository = WalletRepositoryImpl()
