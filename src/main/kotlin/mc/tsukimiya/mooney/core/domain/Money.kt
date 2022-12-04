package mc.tsukimiya.mooney.core.domain

data class Money(val amount: Int) {
    init {
        require(amount >= 0)
    }
}
