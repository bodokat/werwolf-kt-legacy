package roles

import Player
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.entity.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Dieb : BaseHuman {
    override suspend fun before(bot: Kord, player: Player, roles: Map<User, Role>, extra_roles: List<Role>) {
        player.dmChannel.createEmbed {
            title = "Dieb"
            description = "Du bist ein Dieb. Du kannst deine Rolle mit einem Mitspieler tauschen"
        }
    }

    private var toSwap: User? = null

    override suspend fun ask(bot: Kord, player: Player, roles: Map<User, Role>, extra_roles: List<Role>) {
        player.dmChannel.createMessage("Mit wem willst du tauschen?")
        val choice = player.choose(roles.keys.filterNot { it == player.user }, ReactionEmoji.Unicode("\uD83E\uDD1A"))

        toSwap = choice
    }

    override fun action(bot: Kord, player: Player, roles: MutableMap<User, Role>, extra_roles: MutableList<Role>) {
        if (toSwap != null) {
            val newRole = roles[toSwap]!!
            GlobalScope.launch { player.dmChannel.createMessage("$toSwap war $newRole") }
            roles[toSwap!!] = roles[player.user]!!
            roles[player.user] = newRole
        }
    }
}