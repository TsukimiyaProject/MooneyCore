package mc.tsukimiya.mooney.core.domain.exception

import java.util.*

class WalletNotFoundException(val id: UUID) : RuntimeException(id.toString()) {
}
