package me.george.daedwin.game.commands.player.moderation.chat;

import me.george.daedwin.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandAlert implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (Constants.ADMINS.contains(sender.getName()) || sender.isOp() || sender instanceof ConsoleCommandSender) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Too short arguments");
                return false;
            } else {
                String message = "";

                for (int i = 0; i < args.length; i++) {
                    message += args[i] + " ";
                }

                message.trim();

                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 5, 5);
                    p.sendTitle(ChatColor.RED + "[ALERT]", ChatColor.GOLD + message, 1, 3, 4);
                    p.sendActionBar(ChatColor.RED + "Alert!");
                }
            }
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission.");
            return true;
        }
        return true;
    }
}
