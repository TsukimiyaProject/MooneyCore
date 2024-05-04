package mc.tsukimiya.mooney.core.config

import mc.tsukimiya.lib4b.config.DatabaseConnector
import mc.tsukimiya.lib4b.config.exception.ConfigKeyNotFoundException
import mc.tsukimiya.lib4b.config.exception.InvalidConfigValueException
import mc.tsukimiya.mooney.core.infrastructure.dao.Accounts
import mc.tsukimiya.mooney.core.infrastructure.dao.TransactionHistories
import org.bukkit.configuration.file.FileConfiguration
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class DatabaseConnectorImpl : DatabaseConnector() {
    override fun connectDatabase(url: String, driver: String) {
        Database.connect(url, driver)
    }

    override fun createSchema() {
        transaction {
            SchemaUtils.create(Accounts, TransactionHistories)
        }
    }
}
