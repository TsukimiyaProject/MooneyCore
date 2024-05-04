package mc.tsukimiya.mooney.core.domain

import java.util.*

interface AccountRepository {
    /**
     * IDからアカウント取得
     *
     * @param id
     * @return
     */
    fun find(id: UUID): Account?

    /**
     * 名前からアカウント取得
     *
     * @param name
     * @return
     */
    fun findByName(name: Name): Account?

    /**
     * アカウント保存
     *
     * @param account
     */
    fun store(account: Account)
}
