package mc.tsukimiya.mooney.core.domain

import mc.tsukimiya.mooney.core.exception.InvalidReasonException

data class TransactionReason(val value: String) {
    companion object {
        const val MAX_LENGTH = 50
    }

    init {
        if (value.length > MAX_LENGTH) throw InvalidReasonException()
    }
}
