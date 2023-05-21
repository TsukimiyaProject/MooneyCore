package mc.tsukimiya.mooney.core.exception

import java.util.*

class AccountNotFoundException(val id: UUID) : RuntimeException(id.toString())
