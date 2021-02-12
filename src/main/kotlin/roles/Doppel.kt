package roles

import Player
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.entity.User

class Doppel: Role {
    private var copiedRole: Role? = null

    override suspend fun before(bot: Kord, player: Player, roles: Map<User, Role>, extra_roles: List<Role>) {
        player.dmChannel.createEmbed {
            title = "Doppelgängerin"
            description = "Du bist die Doppelgängerin. Du kannst die Rolle von jemand anderem kopieren."
        }

        player.dmChannel.createMessage("Wessen Rolle willst du kopieren?")
        val choice = player.choose(roles.keys.filterNot { it == player.user }, ReactionEmoji.Unicode("\uD83E\uDD1A"))

        copiedRole = choice?.let { roles[it]?.javaClass?.getDeclaredConstructor()?.newInstance() }
    }

    override suspend fun ask(bot: Kord, player: Player, roles: Map<User, Role>, extra_roles: List<Role>) {
        copiedRole?.ask(bot, player, roles, extra_roles)
    }

    override fun action(bot: Kord, player: Player, roles: MutableMap<User, Role>, extra_roles: MutableList<Role>) {
        copiedRole?.action(bot, player, roles, extra_roles)
    }

    override suspend fun after(bot: Kord, player: Player, roles: Map<User, Role>, extra_roles: List<Role>) {
        copiedRole?.after(bot, player, roles, extra_roles)
    }

    override val group: Group
        get() = copiedRole?.group ?: Group.Mensch

    override val team: Team
        get() = copiedRole?.team ?: Team.Dorf

    override fun toString(): String = copiedRole?.toString() ?: "Doppelgängerin"
}