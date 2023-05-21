package mc.tsukimiya.mooney.core.exception

import java.util.*

class WalletNotFoundException(val id: UUID) : RuntimeException(id.toString()) {
}
