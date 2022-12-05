package mc.tsukimiya.mooney.core.exception

import java.util.UUID

class NegativeMoneyException(val id: UUID, msg: String? = null) : RuntimeException(msg) {
}
