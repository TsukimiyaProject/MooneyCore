package mc.tsukimiya.mooney.core

import mc.tsukimiya.lib4b.command.CommandRegistrar
import mc.tsukimiya.lib4b.lang.MessageFormatter
import mc.tsukimiya.mooney.core.config.DatabaseConnectorImpl
import mc.tsukimiya.mooney.core.config.SystemAccount
import mc.tsukimiya.mooney.core.domain.AccountRepository
import mc.tsukimiya.mooney.core.domain.Money
import mc.tsukimiya.mooney.core.domain.TransactionHistoryRepository
import mc.tsukimiya.mooney.core.infrastructure.repository.AccountRepositoryImpl
import mc.tsukimiya.mooney.core.infrastructure.repository.TransactionHistoryRepositoryImpl
import mc.tsukimiya.mooney.core.presentation.api.MooneyCoreAPIImpl
import mc.tsukimiya.mooney.core.presentation.command.*
import mc.tsukimiya.mooney.core.presentation.listener.PlayerJoinListener
import org.bukkit.plugin.java.JavaPlugin

class MooneyCore : JavaPlugin() {
    companion object {
        // 他プラグインでトランザクションとか気にするとき用
        val accountRepository: AccountRepository = AccountRepositoryImpl()
        val historyRepository: TransactionHistoryRepository = TransactionHistoryRepositoryImpl()

        // トランザクションとか気にしないならAPIでいい
        val api: MooneyCoreAPI = MooneyCoreAPIImpl(accountRepository, historyRepository)

        // フォーマッター
        lateinit var formatter: MessageFormatter
            private set
    }

    override fun onEnable() {
        // コンフィグ読み込み
        dataFolder.mkdir()
        saveDefaultConfig()
        config.options().copyDefaults(true)
        saveConfig()

        // メッセージコンフィグ
        formatter = MessageFormatter(config)

        // イベントリスナー登録
        server.pluginManager.registerEvents(PlayerJoinListener(config.getLong("default-money"), formatter.formatMessage("log.create")), this)

        // コマンド登録
        registerCommands()

        // DB接続
        DatabaseConnectorImpl().connect(config)

        // システムアカウント登録
        createSystemAccounts()
    }

    /**
     * コマンドの登録
     *
     */
    private fun registerCommands() {
        val commands = mutableMapOf(
            Pair("givemoney", GiveMoneyCommand()),
            Pair("mymoney", MyMoneyCommand()),
            Pair("paymoney", PayMoneyCommand()),
            Pair("setmoney", SetMoneyCommand()),
            Pair("showmoney", ShowMoneyCommand()),
            Pair("takemoney", TakeMoneyCommand())
        )
        CommandRegistrar.registerCommands(this, commands)
    }

    /**
     * システムアカウントの登録
     *
     */
    private fun createSystemAccounts() {
        SystemAccount.entries.forEach {
            // システムアカウントが存在しない場合は作成、存在する場合は名前だけ更新
            if (!api.existsAccount(it.account.id)) {
                api.storeAccount(it.account.id, it.account.name.value, config.getLong("default-money"), formatter.formatMessage("log.create"))
            } else {
                api.storeAccount(it.account.id, it.account.name.value)
            }
            it.account.money = Money(api.getMoney(it.account.id)!!)
        }
    }
}
