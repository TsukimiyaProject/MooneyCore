package mc.tsukimiya.mooney.core.infrastructure.dao

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class Wallet(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Wallet>(Wallets)

    var money by Wallets.money
}
