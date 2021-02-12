package roles

import Player
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.User

class Werwolf: Role {
    override val group: Group = Group.Wolf
    override val team: Team = Team.Wolf

    override suspend fun before(bot: Kord, player: Player, roles: Map<User, Role>, extra_roles: List<Role>) {
        player.dmChannel.createEmbed {
            title = "Werwolf"
            description = "Du bist ein Werwolf. Du gewinnst, wenn kein Werwolf stirbt."
        }
    }

    override suspend fun ask(bot: Kord, player: Player, roles: Map<User, Role>, extra_roles: List<Role>) {
        val others = roles.filter { (_,role) -> role.group == Group.Wolf }.map { (user,_) -> user }

        if (others.isEmpty()) {
            player.dmChannel.createEmbed {
                title = "Du bist allein"
                description = "Eine Rolle in der Mitte ist:"
                field {
                    name = extra_roles.filter { it.group != Group.Wolf }.randomOrNull()?.toString() ?: "Es gibt keine Rollen in der Mitte"
                }
            }
        } else {
            player.dmChannel.createEmbed {
                title = "Die anderen Werwölfe sind"
                others.forEach { field {
                    inline = true
                    name = it.username
                } }
            }
        }
    }

}