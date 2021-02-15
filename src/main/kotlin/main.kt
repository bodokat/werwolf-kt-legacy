
import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.extensions.Extension
import dev.kord.core.entity.ReactionEmoji
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.delay
import lobby.LobbyManager

suspend fun main() {
    // create HTTP Server because Heroku wants one
    embeddedServer(Netty, port = System.getenv("PORT").toInt()) {
        routing {
            get("/") {
                call.respondText("Nothing to see here")
            }
        }
    }.start(wait = false)

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