package roles

import Player
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.User

class Dorfbewohner: BaseHuman {
    override suspend fun before(bot: Kord, player: Player, roles: Map<User, Role>, extra_roles: List<Role>) {
        player.dmChannel.createEmbed {
            title = "Dorfbewohner"
            description = "Du bist ein einfacher Dorfbewohner ohne besondere Fähigkeiten."
        }
    }
}