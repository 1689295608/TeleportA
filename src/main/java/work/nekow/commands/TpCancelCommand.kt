package work.nekow.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import work.nekow.*

class TpCancelCommandExecutor: CommandExecutor {
    override fun onCommand(
        cs: CommandSender,
        c: Command,
        l: String,
        args: Array<out String>
    ): Boolean {
        if (!checkSender(cs)) return true
        if (args.isEmpty()) {
            cs.sendMessage(language("usage-tpcancel"))
            return true
        }
        val target = foundPlayer(cs, args[0]) ?: return true
        val sender = cs as Player

        if (!checkRequest(target, sender)) return true
        requests[sender]?.remove(target)
        target.sendMessage(language("tpa-canceled"))
        sender.sendMessage(language("tpcancel"))
        return true
    }
}

class TpCancelCommandCompleter: TabCompleter {
    override fun onTabComplete(
        cs: CommandSender,
        c: Command,
        l: String,
        args: Array<out String>
    ): MutableList<String> {
        return requests[cs]?.keys?.map {
            it.name
        }?.toMutableList() ?: mutableListOf()
    }

}