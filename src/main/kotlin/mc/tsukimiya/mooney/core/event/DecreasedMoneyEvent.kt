package mc.tsukimiya.mooney.core.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import java.util.*

class DecreasedMoneyEvent(val playerId: UUID, val amount: Int) : Event() {
    companion object {
        private val handlerList = HandlerList()

        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }
}
