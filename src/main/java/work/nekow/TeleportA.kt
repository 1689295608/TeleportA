package work.nekow

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import work.nekow.commands.*
import work.nekow.utils.LanguageUtil
import java.util.*
import kotlin.io.path.Path

val requests = HashMap<Player, HashMap<Player, Int>>()
lateinit var language: LanguageUtil
lateinit var plugin: JavaPlugin
class TeleportA: JavaPlugin() {
    override fun onEnable() {
        plugin = this
        language = LanguageUtil(Path(dataFolder.path), Path(dataFolder.path, "language.json"))
        val tpa = getCommand("tpa")
        tpa!!.setExecutor(TpaCommandExecutor())
        tpa.tabCompleter = TpaCommandCompleter()
        val tpahere = getCommand("tpahere")
        tpahere!!.setExecutor(TpaHereCommandExecutor())
        tpahere.tabCompleter = TpaHereCommandCompleter()
        val tpaccept = getCommand("tpaccept")
        tpaccept!!.setExecutor(TpacceptCommandExecutor())
        tpaccept.tabCompleter = TpacceptCommandCompleter()
        val tpdeny = getCommand("tpdeny")
        tpdeny!!.setExecutor(TpDenyCommandExecutor())
        tpdeny.tabCompleter = TpDenyCommandCompleter()
        logger.info("TeleportA is enabled")
    }
}
fun language(key: String, vararg args: Any?): String {
    return language.language(key, *args)
}
fun checkSender(sender: CommandSender): Boolean {
    if (sender == Bukkit.getConsoleSender() || sender !is Player) {
        sender.sendMessage(language("only-player"))
        return false
    }
    return true
}
fun foundPlayer(sender: CommandSender, name: String): Player? {
    val target = Bukkit.getPlayer(name)
    if (target == null) {
        sender.sendMessage(language("player-not-found"), name)
        return null
    }
    return target
}
fun checkRequest(sender: Player, target: Player): Boolean {
    if (requests[target] == null || requests[target]?.get(sender) == null) {
        sender.sendMessage(language("tpaccept-no-request"))
        return false
    }
    return true
}