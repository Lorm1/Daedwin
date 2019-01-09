package me.george.daedwin.game.commands;

import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandClearChat implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        /*if (cmd.getName().equalsIgnoreCase("clearchat")) {*/
        if (sender instanceof ConsoleCommandSender) {
            // Spams the chat with 100 empty messages to clear it.
//                for (int i = 0; i < 100; i++)
//                    Bukkit.broadcastMessage(" ");

            Bukkit.broadcastMessage(ChatColor.GOLD + "The chat has been cleared.");
            return true;
        } else if (sender instanceof Player) {
            Player p = (Player) sender;
            DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (!DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId()).isStaff()) { // dont spam the staff
                    for (int i = 0; i < 100; i++)
                        pl.sendMessage(" ");
                }
            }

            Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "The chat has been cleared.");
            return true;
        }
        //return true;
        /*}*/
        return true;
    }
}
