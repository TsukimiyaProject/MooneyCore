package mc.tsukimiya.mooney.core.usecase.dto

import mc.tsukimiya.mooney.core.domain.Account
import java.util.*

data class AccountDto(val id: UUID, val name: String, val money: ULong) {
    companion object {
        fun fromAccount(account: Account): AccountDto {
            return AccountDto(account.id, account.name.value, account.money.amount)
        }
    }
}
