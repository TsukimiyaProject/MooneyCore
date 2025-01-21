package mc.tsukimiya.mooney.core.infrastructure

import mc.tsukimiya.dbconnector.Database
import mc.tsukimiya.mooney.core.domain.model.Money
import mc.tsukimiya.mooney.core.domain.model.MoneyTransaction
import mc.tsukimiya.mooney.core.domain.model.Reason
import mc.tsukimiya.mooney.core.domain.model.Wallet
import mc.tsukimiya.mooney.core.domain.repository.MoneyTransactionHistoryRepository
import java.sql.SQLException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MoneyTransactionHistoryRepositoryImpl : MoneyTransactionHistoryRepository {
    override fun find(wallet: Wallet, start: Int?, offset: Int?): List<MoneyTransaction> {
        val result = mutableListOf<MoneyTransaction>()

        // 両方nullでなければページングする
        val isPaging = start != null && offset != null

        val connection = Database.getConnection() ?: throw SQLException()
        val stmt = connection.prepareStatement(
            """
                    |SELECT
                    |    mt.id,
                    |    mt.player,
                    |    mt.payee,
                    |    mt.amount,
                    |    mt.reason,
                    |    mt.created_at,
                    |    BIN_TO_UUID(wallets.owner) AS owner
                    |FROM money_transactions as mt
                    |LEFT JOIN wallets ON (mt.player = ? AND mt.payee = wallets.id) OR
                    |                     (mt.payee = ? AND mt.player = wallets.id)
                    |WHERE player = ? OR payee = ?
                    |ORDER BY id DESC
                 """.trimMargin() + if (isPaging) " LIMIT ?, ?;" else ";"
        )
        stmt.use {
            stmt.setInt(1, wallet.id)
            stmt.setInt(2, wallet.id)
            stmt.setInt(3, wallet.id)
            stmt.setInt(4, wallet.id)
            if (isPaging) {
                stmt.setInt(5, start)
                stmt.setInt(6, offset)
            }

            val resultSet = stmt.executeQuery()
            resultSet.use {
                while (resultSet.next()) {
                    val from = resultSet.getObject("player") as Int?
                    val to = resultSet.getObject("payee") as Int?
                    val uuid = (resultSet.getObject("owner") as String?)?.let { UUID.fromString(it) }
                    val reason = resultSet.getObject("reason") as String?
                    result.add(
                        MoneyTransaction(
                            resultSet.getInt("id"),
                            from?.let { if (it == wallet.id) wallet else Wallet(it, uuid!!, Money(0)) },
                            to?.let { if (it == wallet.id) wallet else Wallet(it, uuid!!, Money(0)) },
                            Money(resultSet.getInt("amount")),
                            LocalDateTime.parse(
                                resultSet.getString("created_at"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            ),
                            if (reason != null) Reason(reason) else null
                        )
                    )
                }
            }
        }

        return result
    }

    override fun count(id: Int): Int {
        val connection = Database.getConnection() ?: throw SQLException()

        val stmt = connection.prepareStatement(
            """
                |SELECT
                |    COUNT(*)
                |FROM money_transactions
                |WHERE player = ? OR payee = ?;
            """.trimMargin()
        )

        stmt.use {
            stmt.setInt(1, id)
            stmt.setInt(2, id)
            val resultSet = stmt.executeQuery()
            resultSet.use {
                return if (resultSet.next()) resultSet.getInt("COUNT(*)") else 0
            }
        }
    }

    override fun store(event: MoneyTransaction) {
        val connection = Database.getConnection() ?: throw SQLException()

        val stmt = connection.prepareStatement(
            """
                |INSERT INTO money_transactions (player, payee, amount, reason, created_at) VALUES
                |(?, ?, ?, ?, ?);
            """.trimMargin()
        )

        stmt.use {
            stmt.setObject(1, event.from?.id)
            stmt.setObject(2, event.to?.id)
            stmt.setInt(3, event.amount.value)
            stmt.setObject(4, event.reason?.value)
            stmt.setString(5, event.datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))

            stmt.executeUpdate()
        }
    }
}
