package me.george.daedwin.game.commands.moderation.chat;

import me.george.daedwin.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandShout implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //if (cmd.getName().equalsIgnoreCase("shout")) {
            if (Constants.ADMINS.contains(sender.getName()) || sender.isOp() || sender instanceof ConsoleCommandSender) {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.RED + "Too short arguments");
                    return false;
                } else {
//                    String broadcast = "";
//
//                    for (String message : args) {
//                        broadcast = (broadcast + message + " ");
//                    }

                    //Bukkit.broadcastMessage(args[0]);
                    //Bukkit.broadcastMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + ">>> " + ChatColor.AQUA + broadcast);

                    String message = "";

                    for (int i = 0; i < args.length; i++) {
                        message += args[i] + " ";
                    }

                    message.trim();

                    Bukkit.broadcastMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + ">> " + ChatColor.AQUA + message);

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 15, 15);
                    }
                }
            } else {
                sender.sendMessage(ChatColor.DARK_RED + "You do not have permission.");
                return true;
            }
        return true;
        //}
    }
}
