package mc.tsukimiya.mooney.core

import java.time.LocalDateTime
import java.util.*

data class MoneyTransaction(
    val from: UUID?,
    val to: UUID?,
    val amount: Double,
    val reason: String,
    val datetime: LocalDateTime
)
