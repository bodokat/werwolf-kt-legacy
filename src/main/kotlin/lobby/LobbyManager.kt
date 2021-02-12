package lobby

import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.extensions.Extension
import dev.kord.common.entity.Snowflake
import dev.kord.core.any
import dev.kord.core.entity.channel.VoiceChannel
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class LobbyManager(bot:ExtensibleBot): Extension(bot) {
    override val name = "lobby"

    val lobbies = mutableMapOf<Snowflake,Lobby>()



    override suspend fun setup() {

        command {
            name = "join"
            description = "Joins the current lobby (creates a new one if none exists)"

            check { it.message.author?.isBot?.not() ?: false }

            action {
                if (event.guildId == null) {
                    message.channel.createMessage("!join kann nur in einem Server-chat benutzt werden")
                    return@action
                }
                message.author?.let {
                    lobbies.computeIfAbsent(event.guildId!!) { Lobby(bot.kord) }.players.add(it)
                }
            }
        }

        command {
            name = "leave"
            description = "leave the current lobby"

            check { it.message.author?.isBot?.not() ?: false }

            action {
                message.author?.let {
                    lobbies[event.guildId]?.players?.remove(it)
                }
            }
        }

        command {
            name = "invite"
            description = "invite all players in the voice channel you're currently in"

            check { it.message.author?.isBot?.not() ?: false }

            action {
                val voiceChannel = event.getGuild()?.let { it.channels.filterIsInstance<VoiceChannel>().firstOrNull { channel -> channel.voiceStates.any { state -> state.userId == message.author?.id } } }

                if (voiceChannel == null) {
                    message.channel.createMessage("Du musst in einem Sprachkanal sein")
                    return@action
                }

                val users = voiceChannel.voiceStates.map { it.getMember().asUser() }.toList()

                lobbies.computeIfAbsent(event.guildId!!) {Lobby(bot.kord)}.players.addAll(users)
            }
        }

        command {
            name = "start"
            description = "start the game"

            action {
                lobbies[event.guildId]?.start()
            }
        }
    }


}