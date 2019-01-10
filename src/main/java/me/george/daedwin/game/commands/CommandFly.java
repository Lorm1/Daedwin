package me.george.daedwin.game.commands;

import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFly implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        if (!player.isAdmin()) return true;

        if (args.length == 0) {
            if (!p.getAllowFlight()) {
                p.setAllowFlight(true);
                p.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "FLYING - " + ChatColor.GREEN.toString() + ChatColor.BOLD + "ON");
                return true;
            } else {
                p.setAllowFlight(false);
                p.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "FLYING - " + ChatColor.RED.toString() + ChatColor.BOLD + "OFF");
            }
        } else if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            if (!target.isOnline() || target == null) {
                p.sendMessage(ChatColor.RED + "That player is not online.");
                return true;
            }

            if (!target.getAllowFlight()) {
                target.setAllowFlight(true);
                p.sendMessage(ChatColor.GREEN + "Enabled flying for " + ChatColor.YELLOW + target.getName());
                target.sendMessage(ChatColor.GREEN + "You can now fly.");
                return true;
            } else {
                target.setAllowFlight(false);
                p.sendMessage(ChatColor.RED + "Disabled flying for " + ChatColor.YELLOW + target.getName());
                target.sendMessage(ChatColor.RED + "You can no longer fly.");
                return true;
            }
        } else {
            p.sendMessage(ChatColor.RED + "Invalid Usage.");
            return false;
        }
        return true;
    }
}
