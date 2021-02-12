
import dev.kord.core.Kord
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import io.github.cdimascio.dotenv.Dotenv
import kotlinx.coroutines.delay

suspend fun main() {
    val token = Dotenv.load()["TOKEN"]
    val bot = Kord(token)

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

    bot.login()
}
