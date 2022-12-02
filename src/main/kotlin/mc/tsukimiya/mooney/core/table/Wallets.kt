package mc.tsukimiya.mooney.core.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column

object Wallets : UUIDTable() {
    val amount: Column<Int> = integer("amount")
}
