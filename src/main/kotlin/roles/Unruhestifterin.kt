package roles

import Player
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.entity.User

class Unruhestifterin: BaseHuman {
    override suspend fun before(bot: Kord, player: Player, roles: Map<User, Role>, extra_roles: List<Role>) {
        player.dmChannel.createEmbed {
            title = "Unruhestifterin"
            description = "Du bist Unruhestifterin. Du kannst zwei andere Spieler vertauschen"
        }
    }

    var toSwap1: User? = null
    var toSwap2: User? = null

    override suspend fun ask(bot: Kord, player: Player, roles: Map<User, Role>, extra_roles: List<Role>) {
        val choices = player.chooseMany(roles.keys.filterNot { it == player.user }, ReactionEmoji.Unicode("\uD83E\uDD1A"), 2).toList()

        toSwap1 = choices[0]
        toSwap2 = choices[1]
    }

    override fun action(bot: Kord, player: Player, roles: MutableMap<User, Role>, extra_roles: MutableList<Role>) {
        if (toSwap1 != null && toSwap2 != null) {
            val temp = roles[toSwap1]!!
            roles[toSwap1!!] = roles[toSwap2]!!
            roles[toSwap2!!] = temp
        }
    }
}