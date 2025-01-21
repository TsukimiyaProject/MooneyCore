package mc.tsukimiya.mooney.core.infrastructure

import mc.tsukimiya.dbconnector.Database
import mc.tsukimiya.mooney.core.domain.model.Money
import mc.tsukimiya.mooney.core.domain.model.Wallet
import java.sql.SQLException
import java.util.*

class ConsistentWalletRepositoryImpl : SimpleWalletRepositoryImpl() {
    override fun find(id: Int): Wallet? {
        val connection = Database.getConnection() ?: throw SQLException()
        val stmt = connection.prepareStatement(
            """
                |SELECT
                |    wallets.id AS id,
                |    BIN_TO_UUID(wallets.owner) AS owner,
                |    (
                |        (SELECT SUM(mt.amount) FROM money_transactions AS mt WHERE mt.payee = wallets.id) -
                |        (SELECT SUM(mt.amount) FROM money_transactions AS mt WHERE mt.player = wallets.id)
                |    ) AS money
                |FROM wallets
                |WHERE id = ?
            """.trimMargin()
        )
        stmt.use {
            stmt.setInt(1, id)

            val resultSet = stmt.executeQuery()
            resultSet.use {
                while (resultSet.next()) {
                    return Wallet(
                        resultSet.getInt("id"),
                        UUID.fromString(resultSet.getString("owner")),
                        Money(resultSet.getInt("money"))
                    )
                }
            }
        }

        return null
    }

    override fun findByOwner(owner: UUID): Wallet? {
        val connection = Database.getConnection() ?: throw SQLException()
        val stmt = connection.prepareStatement(
            """
                |SELECT
                |    wallets.id AS id,
                |    BIN_TO_UUID(wallets.owner) AS owner,
                |    (
                |        (SELECT SUM(mt.amount) FROM money_transactions AS mt WHERE mt.payee = wallets.id) -
                |        (SELECT SUM(mt.amount) FROM money_transactions AS mt WHERE mt.player = wallets.id)
                |    ) AS money
                |FROM wallets
                |WHERE owner = UUID_TO_BIN(?)
            """.trimMargin()
        )
        stmt.use {
            stmt.setString(1, owner.toString())

            val resultSet = stmt.executeQuery()
            resultSet.use {
                while (resultSet.next()) {
                    return Wallet(
                        resultSet.getInt("id"),
                        UUID.fromString(resultSet.getString("owner")),
                        Money(resultSet.getInt("money"))
                    )
                }
            }
        }

        return null
    }
}
