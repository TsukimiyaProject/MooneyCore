package mc.tsukimiya.mooney.core.domain

import java.util.*

interface WalletRepository {
    companion object

    fun exists(owner: UUID): Boolean

    fun find(owner: UUID): Wallet?

    fun findAll(): Map<UUID, Wallet>

    fun count(): Long

    fun store(wallet: Wallet)

    fun delete(owner: UUID)
}
