package mc.tsukimiya.mooney.core.infrastructure.repository

import mc.tsukimiya.mooney.core.domain.*
import mc.tsukimiya.mooney.core.exception.AccountNotFoundException
import mc.tsukimiya.mooney.core.infrastructure.dao.TransactionHistories
import java.util.*
import mc.tsukimiya.mooney.core.infrastructure.dao.Account as AccountDAO
import mc.tsukimiya.mooney.core.infrastructure.dao.TransactionHistory as HistoryDAO

internal class TransactionHistoryRepositoryImpl : TransactionHistoryRepository {
    override fun find(id: Long): TransactionHistory? {
        val entity = HistoryDAO.findById(id) ?: return null
        val account = entity.account
        return TransactionHistory(
            entity.id.value,
            Account(account.id.value, Name(account.name), Money(account.money)),
            Money(entity.oldMoney),
            Money(entity.newMoney),
            TransactionReason(entity.reason),
            entity.createdAt
        )
    }

    override fun findByAccount(account: UUID): List<TransactionHistory> {
        val result = mutableListOf<TransactionHistory>()

        val entities = HistoryDAO.find { TransactionHistories.account eq account }
        entities.forEach { entity ->
            val accountEntity = entity.account
            result.add(
                TransactionHistory(
                    entity.id.value,
                    Account(accountEntity.id.value, Name(accountEntity.name), Money(accountEntity.money)),
                    Money(entity.oldMoney),
                    Money(entity.newMoney),
                    TransactionReason(entity.reason),
                    entity.createdAt
                )
            )
        }

        return result
    }

    override fun store(history: TransactionHistory) {
        val account = AccountDAO.findById(history.account.id) ?: throw AccountNotFoundException()
        HistoryDAO.new {
            this.account = account
            this.oldMoney = history.oldMoney.amount
            this.newMoney = history.newMoney.amount
            this.reason = history.reason.value
            this.createdAt = history.date
        }
    }
}
