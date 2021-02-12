
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.DmChannel
import dev.kord.core.event.message.ReactionAddEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import roles.Group
import roles.Role
import roles.Team
import kotlin.random.Random

suspend fun game(players: MutableSet<User>,roles: MutableList<Role>, bot: Kord) = coroutineScope {
    assert(roles.size >= players.size)

    val extraRoles = MutableList(roles.size - players.size) { roles.removeAt(Random.nextInt(roles.size)) }
    val playerRoles : MutableMap<User, Role> = mutableMapOf()
    @Suppress("NAME_SHADOWING") val players = players.shuffled().map {
        async {
            val role = roles.removeAt(0)
            playerRoles[it] = role
            Pair(Player.create(it), role)
        }
    }.awaitAll()

    players.map { (player, role) -> async {        role.before(bot, player, playerRoles, extraRoles) } }.awaitAll()
    players.map { (player, role) -> async { role.ask(bot, player, playerRoles, extraRoles) } }.awaitAll()
    players.forEach { (player, role) -> role.action(bot, player, playerRoles, extraRoles) }
    players.map { (player, role) -> async { role.after(bot, player, playerRoles, extraRoles) } }.awaitAll()

    players.map { (player, _) -> async {  player.dmChannel.createMessage("Voting Start") } }.awaitAll()

    val votes = players.asFlow().map { (player, _) ->
         player.choose(players.map { (player, _) -> player.user }, ReactionEmoji.Unicode("✅"))
    }.filterNotNull().fold(mutableMapOf<User, Int>()) {acc, v -> acc.merge(v, 1, Int::plus); acc}

    val maxVotes = votes.values.maxOrNull()

    val deadPlayers = if (maxVotes == null || maxVotes == 1) listOf() else votes.filter { (_, v) -> v == maxVotes }.map { it.key }

    val winningTeam = if (playerRoles.values.any {it.group == Group.Wolf}) {
        if (deadPlayers.any { playerRoles[it]?.group == Group.Wolf }) Team.Dorf else Team.Wolf
    } else {
        if (deadPlayers.isEmpty()) Team.Dorf else Team.Wolf
    }

    val message = if (winningTeam == Team.Dorf) "Das Dorf hat gewonnen" else "Die Werwölfe haben gewonnen"

    players.map { (player, _) -> async { player.dmChannel.createMessage(message) } }.awaitAll()

    val message2 = "Die Gewinner sind: ${
        players.filter { (_, role) -> role.team == winningTeam }.joinToString { (player, _) -> player.user.toString() }
    }\n-----------------"

    players.map { (player, _) -> async { player.dmChannel.createMessage(message2) }  }.awaitAll()
}

data class Player(val user: User, val dmChannel: DmChannel) {
    companion object {
        suspend fun create(user: User) = Player(user, user.getDmChannel())
    }

    suspend fun <T> choose(options: Iterable<T>, reaction: ReactionEmoji): T? = coroutineScope {
        val messages = mutableMapOf<Snowflake,T>()


        val result = async { user.kord.events.filterIsInstance<ReactionAddEvent>().first { event -> event.channel == dmChannel && messages.contains(event.messageId) } }

        options.forEach { option -> launch {
            val msg = dmChannel.createMessage(option.toString())
            messages[msg.id] = option
            msg.addReaction(reaction)
        } }
        return@coroutineScope messages[result.await().messageId]
    }
    suspend fun <T> chooseMany(options: Iterable<T>, reaction: ReactionEmoji, amount: Int): Set<T> = coroutineScope {
        val messages = mutableMapOf<Snowflake,T>()

        val result = mutableSetOf<T>()

        val events = async {
            user.kord.events.collect { event ->
                when (event) {
                    is ReactionAddEvent -> {
                        if (!(event.channelId == dmChannel.id && messages.containsKey(event.messageId))) return@collect
                        result.add(messages[event.messageId]!!)
                        if (result.size == amount) this.cancel()
                    }
                }
            }
        }


        options.forEach { option -> launch {
            val msg = dmChannel.createMessage(option.toString())
            messages[msg.id] = option
            msg.addReaction(reaction)
        } }
        events.await()
        return@coroutineScope result
    }
}

