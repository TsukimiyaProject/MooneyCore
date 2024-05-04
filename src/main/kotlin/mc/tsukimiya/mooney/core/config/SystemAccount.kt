package mc.tsukimiya.mooney.core.config

import mc.tsukimiya.mooney.core.domain.Account
import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.domain.Name
import java.util.*

enum class SystemAccount(val account: Account) {
    ADMIN(Account(UUID(0, 0), Name("月宮計画管理者"), Money(0))),
    BANK(Account(UUID(0, 1), Name("月宮銀行"), Money(0))),
    ITEM_INSURANCE(Account(UUID(0, 2), Name("月宮保険"), Money(0))),
    REAL_ESTATE(Account(UUID(0, 3), Name("月宮不動産"), Money(0)))
}
