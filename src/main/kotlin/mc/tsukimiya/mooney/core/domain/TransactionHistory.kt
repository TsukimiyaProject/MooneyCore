package mc.tsukimiya.mooney.core.domain

import java.time.LocalDateTime

data class TransactionHistory(
    val id: Long,
    val account: Account,
    val oldMoney: Money,
    val newMoney: Money,
    val reason: TransactionReason,
    val date: LocalDateTime
)
