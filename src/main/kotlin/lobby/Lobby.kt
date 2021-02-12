package lobby

import dev.kord.core.Kord
import dev.kord.core.entity.User
import game
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import roles.*

class Lobby(val bot:Kord) {
    val players = mutableSetOf<User>()
    val roles = mutableListOf(
        Doppel(),
        Werwolf(),
        Werwolf(),
        Freimaurer(),
        Freimaurer(),
        Dorfbewohner(),
        Seherin(),
        Dieb(),
        Unruhestifterin(),
        Schlaflose(),
    )

    var currentGame: Job? = null

    val started: Boolean
        get() = currentGame?.isActive ?: false


    fun start() {
        currentGame =  GlobalScope.launch { game(players,roles, bot) }
    }
}