package me.george.daedwin.game.commands;

import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGodMode implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        if (!player.isAdmin()) return true;

        if (args.length == 0) {
            if (!p.isInvulnerable()) {
                p.setInvulnerable(true);
                p.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "GOD MODE - " + ChatColor.GREEN.toString() + ChatColor.BOLD + "ON");
                return true;
            } else {
                p.setInvulnerable(false);
                p.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "GOD MODE - " + ChatColor.RED.toString() + ChatColor.BOLD + "OFF");
            }
        } else if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                p.sendMessage(ChatColor.RED + "That player is not online.");
                return true;
            }

            if (!target.isInvulnerable()) {
                target.setInvulnerable(true);
                p.sendMessage(ChatColor.GREEN + "Enabled God Mode for " + ChatColor.YELLOW + target.getName());
                target.sendMessage(ChatColor.GREEN + "You are now in God Mode.");
                return true;
            } else {
                target.setInvulnerable(false);
                p.sendMessage(ChatColor.RED + "Disabled God Mode for " + ChatColor.YELLOW + target.getName());
                target.sendMessage(ChatColor.RED + "You are no longer in God Mode.");
                return true;
            }
        } else {
            p.sendMessage(ChatColor.RED + "Invalid Usage.");
            return false;
        }
        return true;
    }
}
