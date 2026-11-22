package mc.tsukimiya.mooney.core.infrastructure

import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.core.isNotNull
import org.jetbrains.exposed.v1.core.java.javaUUID
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.datetime.datetime
import java.time.LocalDateTime

object MoneyTransactionsTable : IntIdTable("money_transactions") {
    val from = javaUUID("from").nullable()
    val to = javaUUID("to").nullable()
    val amount = double("amount")
    val reason = text("reason")
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now().toKotlinLocalDateTime() }

    init {
        check("from_or_to_not null") {
            (from.isNotNull()) or (to.isNotNull())
        }
    }
}
