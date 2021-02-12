
import com.kotlindiscord.kord.extensions.ExtensibleBot
import io.github.cdimascio.dotenv.Dotenv
import lobby.LobbyManager

suspend fun main() {
    val token = Dotenv.load()["TOKEN"]
    val bot = ExtensibleBot(token, "!")

    bot.addExtension {
        LobbyManager(it)
    }

    bot.start()
}
