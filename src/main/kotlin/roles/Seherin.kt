package roles

import Player
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.entity.User

class Seherin: BaseHuman {
    override suspend fun before(bot: Kord, player: Player, roles: Map<User, Role>, extra_roles: List<Role>) {
        player.dmChannel.createEmbed {
            title = "Seherin"
            description = "Du bist Seherin. Sieh dir die Rolle eines Mitspielers an (oder eine in der Mitte) und versuche so, die Werwölfe zu finden"
        }
    }

    override suspend fun ask(bot: Kord, player: Player, roles: Map<User, Role>, extra_roles: List<Role>) {
        player.dmChannel.createMessage("Wessen Rolle willst du sehen?")
        val choice = player.choose(roles.keys.filterNot { it != player.user }.plus(object {
            override fun toString(): String = "Eine Rolle aus der Mitte"
        }), ReactionEmoji.Unicode("\uD83D\uDD2E"))

        if (choice is User) {
            player.dmChannel.createMessage("$choice ist ${roles[choice]}")
        } else {
            player.dmChannel.createMessage("Eine Rolle aus der Mitte ist ${extra_roles.random()}")
        }
    }
}