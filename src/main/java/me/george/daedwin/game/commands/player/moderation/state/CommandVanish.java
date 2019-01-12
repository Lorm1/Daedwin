package me.george.daedwin.game.commands.player.moderation.state;

import me.george.daedwin.Daedwin;
import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandVanish implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        if (!player.isAdmin()) return true;

        if (args.length == 0) {
            if (!Daedwin._hiddenPlayers.contains(p)) {
                Daedwin._hiddenPlayers.add(p);
                for (Player pl : Bukkit.getOnlinePlayers()) pl.hidePlayer(p);

                p.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "VANISH - " + ChatColor.GREEN.toString() + ChatColor.BOLD + "ON");
                return true;
            } else {
                Daedwin._hiddenPlayers.remove(p);
                for (Player pl : Bukkit.getOnlinePlayers()) pl.showPlayer(p);

                p.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "VANISH - " + ChatColor.RED.toString() + ChatColor.BOLD + "OFF");
                return true;
            }
        } else if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            if (!target.isOnline() || target == null) {
                p.sendMessage(ChatColor.RED + "That player is not online.");
                return true;
            }

            if (!Daedwin._hiddenPlayers.contains(target)) {
                Daedwin._hiddenPlayers.add(target);
                p.sendMessage(ChatColor.GREEN + "Vanished player " + ChatColor.YELLOW + target.getName());
                target.sendMessage(ChatColor.GREEN  + "You were vanished by " + ChatColor.YELLOW + p.getName());

                for (Player pl : Bukkit.getOnlinePlayers()) pl.hidePlayer(target);
                return true;
            } else {
                Daedwin._hiddenPlayers.remove(p);
                p.sendMessage(ChatColor.RED + "Unvanished player " + ChatColor.YELLOW + target.getName());
                target.sendMessage(ChatColor.RED + "You were unvanished by " + ChatColor.YELLOW + p.getName());

                for (Player pl : Bukkit.getOnlinePlayers()) pl.showPlayer(target);
            }
        } else {
            p.sendMessage(ChatColor.RED + "Invalid Usage.");
            return false;
        }
        return true;
    }
}
