package mc.tsukimiya.mooney.core.exception

import java.util.*

class InvalidMoneyAmountException(val id: UUID) : RuntimeException(id.toString())
