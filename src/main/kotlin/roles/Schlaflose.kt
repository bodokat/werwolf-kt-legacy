package roles

import Player
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.User

class Schlaflose: BaseHuman {
    override suspend fun before(bot: Kord, player: Player, roles: Map<User, Role>, extra_roles: List<Role>) {
        player.dmChannel.createEmbed {
            title = "Schlaflose"
            description = "Du bist die Schlaflose. Am Ende der Nacht erfährst du deine neue Rolle"
        }
    }

    override suspend fun after(bot: Kord, player: Player, roles: Map<User, Role>, extra_roles: List<Role>) {
        player.dmChannel.createMessage("Deine Rolle ist jetzt ${roles[player.user]}")
    }
}