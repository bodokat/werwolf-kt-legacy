
import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.extensions.Extension
import dev.kord.core.entity.ReactionEmoji
import io.github.cdimascio.dotenv.Dotenv
import kotlinx.coroutines.delay
import lobby.LobbyManager

suspend fun main() {
    val token = Dotenv.configure().ignoreIfMissing().load()["TOKEN"]
    val bot = ExtensibleBot(token, "!")

    bot.addExtension {
        LobbyManager(it)
    }

    bot.start()
}


class PingExtension(bot: ExtensibleBot): Extension(bot) {
    override val name = "ping"

    override suspend fun setup() {
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