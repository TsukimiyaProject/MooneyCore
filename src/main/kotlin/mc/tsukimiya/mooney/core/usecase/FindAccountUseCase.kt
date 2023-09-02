package mc.tsukimiya.mooney.core.usecase

import mc.tsukimiya.mooney.core.domain.AccountRepository
import mc.tsukimiya.mooney.core.exception.AccountNotFoundException
import mc.tsukimiya.mooney.core.usecase.dto.AccountDto
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class FindAccountUseCase(private val repository: AccountRepository) {
    fun execute(id: UUID): AccountDto {
        return transaction {
            val account = repository.find(id) ?: throw AccountNotFoundException(id)

            AccountDto.fromAccount(account)
        }
    }
}
