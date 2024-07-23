package work.nekow.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import work.nekow.*

class TpDenyCommandExecutor: CommandExecutor {
    override fun onCommand(
        cs: CommandSender,
        c: Command,
        l: String,
        args: Array<out String>
    ): Boolean {
        if (!checkSender(cs)) return true
        if (args.isEmpty()) {
            cs.sendMessage(language("usage-tpdeny"))
            return true
        }
        val target = foundPlayer(cs, args[0]) ?: return true
        val sender = cs as Player
        if (!checkRequest(sender, target)) return true
        target.sendMessage(language("tpa-tpdeny", sender.name))
        sender.sendMessage(language("tpdeny"))
        requests[target]?.remove(sender)
        return true
    }
}
class TpDenyCommandCompleter: TabCompleter {
    override fun onTabComplete(
        cs: CommandSender,
        c: Command,
        l: String,
        args: Array<out String>
    ): MutableList<String> {
        when(args.size) {
            0 -> {
                return Bukkit.getOnlinePlayers().map { it.name }.toMutableList()
            }
        }
        return Bukkit.getOnlinePlayers().map { it.name }.toMutableList()
    }
}