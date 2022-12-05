package mc.tsukimiya.mooney.core.exception

import java.util.*

class WalletNotFoundException(val id: UUID, message: String? = null) : RuntimeException(message) {
}
