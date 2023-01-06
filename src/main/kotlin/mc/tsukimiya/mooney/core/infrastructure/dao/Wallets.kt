package mc.tsukimiya.mooney.core.infrastructure.dao

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column

object Wallets : UUIDTable() {
    val money: Column<Int> = integer("money")
}
