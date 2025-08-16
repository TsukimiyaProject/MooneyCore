package mc.tsukimiya.mooney.core.domain.model

data class Money(val value: Int) {
    companion object {
        const val MIN_VALUE = 0
        const val MAX_VALUE = 999999999

        fun validate(value: Int): Boolean {
            return value in MIN_VALUE..MAX_VALUE
        }
    }

    init {
        require(validate(value))
    }
}
