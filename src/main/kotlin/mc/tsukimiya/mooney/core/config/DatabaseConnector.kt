package mc.tsukimiya.mooney.core.config

import mc.tsukimiya.lib4b.config.exception.ConfigKeyNotFoundException
import mc.tsukimiya.lib4b.config.exception.InvalidConfigValueException
import mc.tsukimiya.mooney.core.infrastructure.dao.Wallets
import org.bukkit.configuration.file.FileConfiguration
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class DatabaseConnector {
    fun connect(config: FileConfiguration) {
        when (val type = config.getString("db-type")) {
            "sqlite" -> {
                val fileName = config.getString("sqlite.file") ?: throw ConfigKeyNotFoundException("sqlite.file")
                val file = File("database" + File.separator + fileName)
                if (!file.exists()) {
                    Files.createDirectories(Paths.get(file.parent))
                    Files.createFile(Paths.get(file.path))
                }
                Database.connect("jdbc:sqlite:${file.path}", "org.sqlite.JDBC")
            }
            "mysql" -> {
                val address = config.getString("mysql.address") ?: throw ConfigKeyNotFoundException("mysql.address")
                val port = config.getInt("mysql.port", -1).let {
                    if (it == -1) throw ConfigKeyNotFoundException("mysql.port") else it
                }
                val db = config.getString("mysql.database") ?: throw ConfigKeyNotFoundException("mysql.database")
                val user = config.getString("mysql.user") ?: throw ConfigKeyNotFoundException("mysql.user")
                val password = config.getString("mysql.password") ?: throw ConfigKeyNotFoundException("mysql.password")
                Database.connect("jdbc:mysql://${address}:${port}/${db}", "com.mysql.jdbc.Driver", user, password)
            }
            else -> throw InvalidConfigValueException("db-type", type ?: "")
        }

        transaction {
            SchemaUtils.create(Wallets)
        }
    }
}
