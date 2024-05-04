package mc.tsukimiya.mooney.core.infrastructure.dao

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TransactionHistory(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TransactionHistory>(TransactionHistories)

    var account by Account referencedOn TransactionHistories.account
    var oldMoney by TransactionHistories.oldMoney
    var newMoney by TransactionHistories.newMoney
    var reason by TransactionHistories.reason
    var createdAt by TransactionHistories.createdAt
}
