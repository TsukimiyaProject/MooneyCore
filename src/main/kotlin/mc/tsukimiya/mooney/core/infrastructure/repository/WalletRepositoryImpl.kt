package mc.tsukimiya.mooney.core.infrastructure.repository

import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.domain.Wallet
import mc.tsukimiya.mooney.core.domain.WalletRepository
import java.util.*
import mc.tsukimiya.mooney.core.infrastructure.dao.Wallet as WalletDao

class WalletRepositoryImpl : WalletRepository {
    override fun exists(owner: UUID): Boolean {
        return WalletDao.findById(owner) != null
    }

    override fun find(owner: UUID): Wallet? {
        val wallet = WalletDao.findById(owner) ?: return null
        return Wallet(owner, Money(wallet.money))
    }

    override fun findAll(): Map<UUID, Wallet> {
        val wallets = mutableMapOf<UUID, Wallet>()
        WalletDao.all().forEach {
            wallets[it.id.value] = Wallet(it.id.value, Money(it.money))
        }

        return wallets
    }

    override fun count(): Long {
        return WalletDao.count()
    }

    override fun store(wallet: Wallet) {
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

fun WalletRepository.Companion.newInstance(): WalletRepository = WalletRepositoryImpl()
