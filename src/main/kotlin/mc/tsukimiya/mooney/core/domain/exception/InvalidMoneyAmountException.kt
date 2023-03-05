package mc.tsukimiya.mooney.core.domain.exception

class InvalidMoneyAmountException(value: Int) : RuntimeException("wrong value : $value")
