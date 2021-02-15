
import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.extensions.Extension
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.event.gateway.ReadyEvent
import kotlinx.coroutines.delay
import lobby.LobbyManager

suspend fun main() {

    val token = System.getenv("TOKEN")
    val bot = ExtensibleBot(token, "!")

    bot.addExtension {
        LobbyManager(it)
    }
    bot.addExtension {
        PingExtension(it)
    }

    bot.start()
}


class PingExtension(bot: ExtensibleBot): Extension(bot) {
    override val name = "ping"

    override suspend fun setup() {
        event<ReadyEvent> {
            println("Ready!")
        }

        command {
            name = "ping"

            action {
                val res = message.channel.createMessage("Pong\nDiese Nachricht wird sich in 5 Sekunden selbst zerstören")
                res.addReaction(ReactionEmoji.Unicode("\uD83C\uDFD3"))
                delay(5000)
                res.delete()
            }
        }
    }

}