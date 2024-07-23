package work.nekow.commands

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import work.nekow.checkSender
import work.nekow.foundPlayer
import work.nekow.language
import work.nekow.requests

class TpaCommandExecutor: CommandExecutor {
    override fun onCommand(
        cs: CommandSender,
        c: Command,
        l: String,
        args: Array<out String>
    ): Boolean {
        if (!checkSender(cs)) return true
        if (args.isEmpty()) {
            cs.sendMessage(language("usage-tpa"))
            return true
        }
        val target = foundPlayer(cs, args[0]) ?: return true
        val sender = cs as Player
        target.spigot().sendMessage(
            ComponentBuilder()
                .append(language("tpa-message", cs.name))
                .append(language("tpa-button-accept"))
                .event(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept ${cs.name}"))
                .append(language("tpa-button-deny"))
                .event(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny ${cs.name}"))
                .build()
        )
        if (requests[sender] == null) {
            requests[sender] = hashMapOf()
        }
        requests[sender]!![target] = 0
        sender.sendMessage(language("tpahere-requested", args[0]))
        return true
    }
}

class TpaCommandCompleter: TabCompleter {
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