package mc.tsukimiya.mooney.core.infrastructure.dao

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column

object Accounts : UUIDTable() {
    val name: Column<String> = varchar("name", 16)
    val money: Column<ULong> = ulong("money")
}
