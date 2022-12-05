package mc.tsukimiya.mooney.core.exception

import java.util.*

class NegativeMoneyException(val id: UUID, msg: String? = null) : RuntimeException(msg) {
}
