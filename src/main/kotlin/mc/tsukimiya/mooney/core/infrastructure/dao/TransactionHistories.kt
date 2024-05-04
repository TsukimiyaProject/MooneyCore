package mc.tsukimiya.mooney.core.infrastructure.dao

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object TransactionHistories : LongIdTable("transaction_histories") {
    val account = reference("account", Accounts)
    val oldMoney = long("old_money")
    val newMoney = long("new_money")
    val reason = varchar("reason", 50)
    val createdAt = datetime("created_at")
}
