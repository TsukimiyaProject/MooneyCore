package mc.tsukimiya.mooney.core.infrastructure

import mc.tsukimiya.mooney.core.PagedResult
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import java.util.*

class MoneyTransactionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MoneyTransactionEntity>(MoneyTransactionsTable) {
        fun getPagedTransactions(player: UUID, page: Int, pageSize: Int): PagedResult<MoneyTransactionEntity> {
            val query = find {
                (MoneyTransactionsTable.from eq player) or (MoneyTransactionsTable.to eq player)
            }

            // 全件数を取得して総ページ数を取得
            val totalCount = query.count()
            val totalPages = (totalCount + pageSize - 1) / pageSize

            val currentPage = if (totalPages < page) totalPages else page.toLong()

            val items = query
                .orderBy(MoneyTransactionsTable.createdAt to SortOrder.DESC)
                .offset(((currentPage - 1) * pageSize))
                .limit(pageSize)
                .toList()

            return PagedResult(items, currentPage.toInt(), totalPages.toInt())
        }
    }

    var from by MoneyTransactionsTable.from
    var to by MoneyTransactionsTable.to
    var amount by MoneyTransactionsTable.amount
    var reason by MoneyTransactionsTable.reason
    val createdAt by MoneyTransactionsTable.createdAt
}
