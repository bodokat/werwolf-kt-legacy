package roles

import Player
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.User

class Freimaurer: BaseHuman {
    override suspend fun before(bot: Kord, player: Player, roles: Map<User, Role>, extra_roles: List<Role>) {
        player.dmChannel.createEmbed {
            title = "Freimaurer"
            description = "Du bist ein Freimaurer. Du weißt, er die anderen Freimaurer sind"
        }
    }

    override suspend fun ask(bot: Kord, player: Player, roles: Map<User, Role>, extra_roles: List<Role>) {
        val others = roles.filter { (_,role) -> role.group == Group.Freimaurer }.map { (user,_) -> user }

        if (others.isEmpty()) {
            player.dmChannel.createEmbed {
                title = "Du bist allein"
            }
        } else {
            player.dmChannel.createEmbed {
                title = "Die anderen Freimaurer sind"
                others.forEach { field {
                    inline = true
                    name = it.username
                } }
            }
        }
    }

}