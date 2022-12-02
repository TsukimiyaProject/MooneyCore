package mc.tsukimiya.mooney.core.entity

import mc.tsukimiya.mooney.core.table.Wallets
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class Wallet(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Wallet>(Wallets)

    var amount by Wallets.amount
}
