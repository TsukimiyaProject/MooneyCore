package mc.tsukimiya.mooney.core.infrastructure.dao

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime

object Accounts : UUIDTable("accounts") {
    val name = varchar("name", 16).uniqueIndex()
    val money = long("money")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}
