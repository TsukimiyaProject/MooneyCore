package mc.tsukimiya.mooney.core.domain.repository

import mc.tsukimiya.mooney.core.domain.model.Wallet
import java.util.*

interface WalletRepository {
    fun find(id: Int): Wallet?

    fun findByOwner(owner: UUID): Wallet?

    fun store(wallet: Wallet)
}
