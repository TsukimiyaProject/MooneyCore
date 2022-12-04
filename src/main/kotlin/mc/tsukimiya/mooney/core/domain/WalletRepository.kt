package mc.tsukimiya.mooney.core.domain

import java.util.*

interface WalletRepository {
    fun exists(id: UUID): Boolean

    fun find(id: UUID): Money?

    fun findAll(): Map<UUID, Money>

    fun count(): Long

    fun save(id: UUID, money: Money)

    fun delete(id: UUID)
}
