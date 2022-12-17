package mc.tsukimiya.mooney.core.infrastructure.repository

import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.domain.WalletRepository
import mc.tsukimiya.mooney.core.infrastructure.entity.Wallet
import java.util.*

class WalletRepositoryImpl : WalletRepository {
    override fun exists(id: UUID): Boolean {
        return Wallet.findById(id) != null
    }

    override fun find(id: UUID): Money? {
        val wallet = Wallet.findById(id)
        return if (wallet != null) Money(wallet.money) else null
    }

    override fun findAll(): Map<UUID, Money> {
        val wallets = mutableMapOf<UUID, Money>()
        Wallet.all().forEach {
            wallets[it.id.value] = Money(it.money)
        }

        return wallets
    }

    override fun count(): Long {
        return Wallet.count()
    }

    override fun save(id: UUID, money: Money) {
        val wallet = Wallet.findById(id)
        if (wallet != null) {
            wallet.money = money.amount
        } else {
            Wallet.new(id) {
                this.money = money.amount
            }
        }
    }

    override fun delete(id: UUID) {
        val wallet = Wallet.findById(id)
        wallet?.delete()
    }
}
