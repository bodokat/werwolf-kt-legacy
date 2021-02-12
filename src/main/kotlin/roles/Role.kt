package roles

import Player
import dev.kord.core.Kord
import dev.kord.core.entity.User

interface Role {
    val group: Group
    val team: Team

    suspend fun before(bot: Kord,player: Player, roles: Map<User,Role>, extra_roles: List<Role>)
    suspend fun ask(bot: Kord,player: Player, roles: Map<User,Role>, extra_roles: List<Role>) {}
    fun action(bot: Kord,player: Player, roles: MutableMap<User,Role>, extra_roles: MutableList<Role>) {}
    suspend fun after(bot: Kord,player: Player, roles: Map<User,Role>, extra_roles: List<Role>) {}
}

interface BaseHuman: Role {
    override val group: Group
        get() = Group.Mensch
    override val team: Team
        get() = Team.Dorf
}

enum class Group {
    Mensch,
    Freimaurer,
    Wolf,
}

enum class Team {
    Dorf,
    Wolf,
}

