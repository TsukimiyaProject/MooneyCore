package mc.tsukimiya.mooney.core.infrastructure

import mc.tsukimiya.dbconnector.Database
import mc.tsukimiya.mooney.core.domain.model.Money
import mc.tsukimiya.mooney.core.domain.model.Wallet
import mc.tsukimiya.mooney.core.domain.repository.WalletRepository
import java.sql.SQLException
import java.util.*

open class SimpleWalletRepositoryImpl : WalletRepository {
    override fun find(id: Int): Wallet? {
        val connection = Database.getConnection() ?: throw SQLException()
        val stmt = connection.prepareStatement(
            """
                |SELECT
                |    id,
                |    BIN_TO_UUID(owner) AS owner,
                |    money
                |FROM wallets
                |WHERE id = ?
            """.trimMargin()
        )
        stmt.use {
            stmt.setInt(1, id)

            val result = stmt.executeQuery()
            result.use {
                if (result.next()) {
                    return Wallet(
                        result.getInt("id"),
                        UUID.fromString(result.getString("owner")),
                        Money(result.getInt("money"))
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
                |    id,
                |    BIN_TO_UUID(owner) AS owner,
                |    money
                |FROM wallets
                |WHERE owner = UUID_TO_BIN(?)
            """.trimMargin()
        )
        stmt.use {
            stmt.setString(1, owner.toString())

            val result = stmt.executeQuery()
            result.use {
                if (result.next()) {
                    return Wallet(
                        result.getInt("id"),
                        UUID.fromString(result.getString("owner")),
                        Money(result.getInt("money"))
                    )
                }
            }
        }
        return null
    }

    override fun store(wallet: Wallet) {
        val connection = Database.getConnection() ?: throw SQLException()
        if (wallet is Wallet.NewWallet) {
            val stmt = connection.prepareStatement("INSERT INTO wallets (owner, money) VALUES (UUID_TO_BIN(?), ?)")
            stmt.use {
                stmt.setString(1, wallet.owner.toString())
                stmt.setInt(2, wallet.money.value)
                stmt.executeUpdate()
            }
        } else {
            val stmt = connection.prepareStatement("UPDATE wallets SET money = ? WHERE id = ?")
            stmt.use {
                stmt.setInt(1, wallet.money.value)
                stmt.setInt(2, wallet.id)
                stmt.executeUpdate()
            }
        }
    }
}
