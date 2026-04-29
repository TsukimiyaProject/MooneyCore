package mc.tsukimiya.mooney.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.toJavaLocalDateTime
import mc.tsukimiya.mooney.core.bukkit.command.*
import mc.tsukimiya.mooney.core.bukkit.listener.PlayerJoinListener
import mc.tsukimiya.mooney.core.infrastructure.MoneyTransactionEntity
import mc.tsukimiya.mooney.core.infrastructure.MoneyTransactionsTable
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.sum
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class MooneyCore : JavaPlugin(), Economy {
    lateinit var messages: FileConfiguration
        private set

    override fun onEnable() {
        saveDefaultConfig()

        messages = YamlConfiguration()
        messages.setDefaults(YamlConfiguration.loadConfiguration(getResource("messages.yml")!!.reader()))

        Database.connect(mc.tsukimiya.dbconnector.Database.getDataSource())
        transaction {
            SchemaUtils.create(MoneyTransactionsTable)
        }

        server.servicesManager.register(Economy::class.java, this, this, ServicePriority.Highest)
        server.pluginManager.registerEvents(PlayerJoinListener(this), this)

        val command = MoneyCommand(this)
        command.registerSubCommands(GiveCommand(this), HelpCommand(this), PayCommand(this), TakeCommand(this), LogCommand(this))
        getCommand("money")?.setExecutor(command)
    }

    override fun getBalance(player: OfflinePlayer?): Double {
        if (player == null) return 0.0

        return transaction {
            // 集計用のSUMカラムを定義
            val sumColumn = MoneyTransactionsTable.amount.sum()

            // DB側で条件に一致するamountの合計値を算出
            val deposit = MoneyTransactionsTable
                .select(sumColumn)
                .where { MoneyTransactionsTable.to eq player.uniqueId }
                .singleOrNull()
            val withdraw = MoneyTransactionsTable
                .select(sumColumn)
                .where { MoneyTransactionsTable.from eq player.uniqueId }
                .singleOrNull()

            (deposit?.get(sumColumn) ?: 0.0) - (withdraw?.get(sumColumn) ?: 0.0)
        }
    }

    fun withdrawPlayer(player: OfflinePlayer?, amount: Double, reason: String): EconomyResponse {
        if (player == null)
            return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Player not found.")
        if (amount < 0)
            return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Amount must be non-negative.")

        val balance = getBalance(player)
        if (balance < amount)
            return EconomyResponse(0.0, balance, EconomyResponse.ResponseType.FAILURE, "Don't have enough money.")

        CoroutineScope(Dispatchers.IO).launch {
            suspendTransaction {
                MoneyTransactionEntity.new {
                    this.from = player.uniqueId
                    this.to = null
                    this.amount = amount
                    this.reason = reason
                }
            }
        }

        return EconomyResponse(amount, balance - amount, EconomyResponse.ResponseType.SUCCESS, "")
    }

    fun depositPlayer(player: OfflinePlayer?, amount: Double, reason: String): EconomyResponse {
        if (player == null)
            return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Player not found.")
        if (amount < 0)
            return EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Amount must be non-negative.")

        val balance = getBalance(player)

        CoroutineScope(Dispatchers.IO).launch {
            suspendTransaction {
                MoneyTransactionEntity.new {
                    this.from = null
                    this.to = player.uniqueId
                    this.amount = amount
                    this.reason = reason
                }
            }
        }

        return EconomyResponse(amount, balance + amount, EconomyResponse.ResponseType.SUCCESS, "")
    }

    fun pay(from: OfflinePlayer, to: OfflinePlayer, amount: Double, reason: String): Pair<EconomyResponse, EconomyResponse> {
        val fromBalance = getBalance(from)
        val toBalance = getBalance(to)
        if (fromBalance < amount) return Pair(
            EconomyResponse(0.0, fromBalance, EconomyResponse.ResponseType.FAILURE, "Don't have enough money."),
            EconomyResponse(0.0, toBalance, EconomyResponse.ResponseType.FAILURE, "Don't have enough money.")
        )

        CoroutineScope(Dispatchers.IO).launch {
            suspendTransaction {
                MoneyTransactionEntity.new {
                    this.from = from.uniqueId
                    this.to = to.uniqueId
                    this.amount = amount
                    this.reason = reason
                }
            }
        }

        return Pair(
            EconomyResponse(amount, fromBalance - amount, EconomyResponse.ResponseType.SUCCESS, ""),
            EconomyResponse(amount, toBalance + amount, EconomyResponse.ResponseType.SUCCESS, "")
        )
    }

    fun getPagedTransaction(player: OfflinePlayer, page: Int): PagedResult<MoneyTransaction> {
        val items = mutableListOf<MoneyTransaction>()
        val query = transaction {
            MoneyTransactionEntity.getPagedTransactions(player.uniqueId, page, config.getInt("page-size", 10))
        }
        query.items.forEach {
            items.add(MoneyTransaction(it.from, it.to, it.amount, it.reason, it.createdAt.toJavaLocalDateTime()))
        }
        return PagedResult(items, query.currentPage, query.totalPages)
    }

    // =============================== 以下はVault用のoverrideメソッド ===============================

    override fun hasBankSupport() = false

    override fun fractionalDigits() = 0

    override fun format(amount: Double) = "%,.0f".format(amount) + currencyNameSingular()

    override fun currencyNamePlural() = currencyNameSingular()

    override fun currencyNameSingular() = config.getString("money-unit", "円")!!

    @Deprecated("Deprecated in Java")
    override fun hasAccount(playerName: String?) = playerName != null

    override fun hasAccount(player: OfflinePlayer?) = player != null

    @Deprecated("Deprecated in Java")
    override fun hasAccount(playerName: String?, worldName: String?) = playerName != null

    override fun hasAccount(player: OfflinePlayer?, worldName: String?) = player != null

    @Deprecated("Deprecated in Java")
    override fun getBalance(playerName: String?): Double {
        return if (playerName != null) getBalance(Bukkit.getOfflinePlayer(playerName)) else 0.0
    }

    @Deprecated("Deprecated in Java")
    override fun getBalance(playerName: String?, world: String?) = getBalance(playerName)

    override fun getBalance(player: OfflinePlayer?, world: String?) = getBalance(player)

    @Deprecated("Deprecated in Java")
    override fun has(playerName: String?, amount: Double) =
        if (playerName != null) has(Bukkit.getOfflinePlayer(playerName), amount) else false

    override fun has(player: OfflinePlayer?, amount: Double) =
        if (player != null) amount <= getBalance(player) else false

    @Deprecated("Deprecated in Java")
    override fun has(playerName: String?, worldName: String?, amount: Double) = has(playerName, amount)

    override fun has(player: OfflinePlayer?, worldName: String?, amount: Double) = has(player, amount)

    @Deprecated("Deprecated in Java")
    override fun withdrawPlayer(playerName: String?, amount: Double) =
        if (playerName != null) withdrawPlayer(Bukkit.getOfflinePlayer(playerName), amount)
        else EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Player not found.")

    override fun withdrawPlayer(player: OfflinePlayer?, amount: Double) = withdrawPlayer(player, amount, "")

    @Deprecated("Deprecated in Java")
    override fun withdrawPlayer(playerName: String?, worldName: String?, amount: Double) =
        withdrawPlayer(playerName, amount)

    override fun withdrawPlayer(player: OfflinePlayer?, worldName: String?, amount: Double) =
        withdrawPlayer(player, amount)

    @Deprecated("Deprecated in Java")
    override fun depositPlayer(playerName: String?, amount: Double) =
        if (playerName != null) depositPlayer(Bukkit.getOfflinePlayer(playerName), amount)
        else EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Player not found.")

    override fun depositPlayer(player: OfflinePlayer?, amount: Double) = depositPlayer(player, amount, "")

    @Deprecated("Deprecated in Java")
    override fun depositPlayer(playerName: String?, worldName: String?, amount: Double) =
        depositPlayer(playerName, amount)

    override fun depositPlayer(player: OfflinePlayer?, worldName: String?, amount: Double) =
        depositPlayer(player, amount)

    @Deprecated("Deprecated in Java")
    override fun createBank(name: String?, player: String?) =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "")

    override fun createBank(name: String?, player: OfflinePlayer?) =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "")

    override fun deleteBank(name: String?) =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "")

    override fun bankBalance(name: String?) =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "")

    override fun bankHas(name: String?, amount: Double) =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "")

    override fun bankWithdraw(name: String?, amount: Double) =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "")

    override fun bankDeposit(name: String?, amount: Double) =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "")

    @Deprecated("Deprecated in Java")
    override fun isBankOwner(name: String?, playerName: String?) =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "")

    override fun isBankOwner(name: String?, player: OfflinePlayer?) =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "")

    @Deprecated("Deprecated in Java")
    override fun isBankMember(name: String?, playerName: String?) =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "")

    override fun isBankMember(name: String?, player: OfflinePlayer?) =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "")

    override fun getBanks(): List<String?> = listOf()

    @Deprecated("Deprecated in Java")
    override fun createPlayerAccount(playerName: String?) = playerName != null

    override fun createPlayerAccount(player: OfflinePlayer?) = player != null

    @Deprecated("Deprecated in Java")
    override fun createPlayerAccount(playerName: String?, worldName: String?) = playerName != null

    override fun createPlayerAccount(player: OfflinePlayer?, worldName: String?) = player != null
}
