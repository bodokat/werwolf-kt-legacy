
import com.kotlindiscord.kord.extensions.ExtensibleBot
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.message.MessageCreateEvent
import io.github.cdimascio.dotenv.Dotenv
import kotlinx.coroutines.delay
import lobby.LobbyManager

suspend fun main() {
    val token = Dotenv.load()["TOKEN"]
    val bot = ExtensibleBot(token, "!")

    bot.addExtension {
        LobbyManager(it)
    }

    bot.on<ReadyEvent> {
        print("Ready!")
    }

    bot.on<MessageCreateEvent> {
        if (message.content == "!ping") {
            val response = message.channel.createMessage("Pong!")
            response.addReaction(ReactionEmoji.Unicode("✅"))

            delay(5000)
            response.delete()
        }
    }

    bot.start()
}
