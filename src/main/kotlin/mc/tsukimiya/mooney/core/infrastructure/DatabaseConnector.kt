package mc.tsukimiya.mooney.core.infrastructure

import mc.tsukimiya.mooney.core.infrastructure.table.Wallets
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

class DatabaseConnector(private val plugin: JavaPlugin) {
    fun connect() {
        when (plugin.config.getString("db-type")) {
            "sqlite" -> {
                val path = plugin.dataFolder.absolutePath + "/" + plugin.config.getString("sqlite.file")
                File(path).createNewFile()
                Database.connect("jdbc:sqlite:$path", "org.sqlite.JDBC")
            }
            "mysql" -> {
                val address = plugin.config.getString("mysql.address") ?: throw RuntimeException("コンフィグが適切じゃないです")
                val port = plugin.config.getInt("mysql.port")
                val db = plugin.config.getString("mysql.database") ?: throw RuntimeException("コンフィグが適切じゃないです")
                val user = plugin.config.getString("mysql.user") ?: throw RuntimeException("コンフィグが適切じゃないです")
                val password = plugin.config.getString("mysql.password") ?: throw RuntimeException("コンフィグが適切じゃないです")
                Database.connect("jdbc:mysql://${address}:${port}/${db}", "com.mysql.jdbc.Driver", user, password)
            }
            else -> throw RuntimeException("コンフィグが適切じゃないです")
        }

        transaction {
            SchemaUtils.create(Wallets)
        }
    }
}
