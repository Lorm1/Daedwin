package me.george.daedwin.game.commands.essentials;

import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandTeleport implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof ConsoleCommandSender) return true;

        Player p = (Player) sender;
        DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        if (!player.isAdmin()) return true;

        if (args.length == 1) { // /tp <player>
            Player target = Bukkit.getPlayer(args[0]);
            if (target.isOnline()) {
                p.teleport(target.getLocation());
                p.sendMessage(ChatColor.GREEN + "Teleported you to " + ChatColor.YELLOW + target.getDisplayName());
            } else {
                p.sendMessage(ChatColor.RED + "That player is offline.");
                return true;
            }
        } else if (args.length == 2) { // /tp <player1> <player2>
            Player target1 = Bukkit.getPlayer(args[0]);
            Player target2 = Bukkit.getPlayer(args[1]);

            if (target1 == null) {
                p.sendMessage(ChatColor.RED + "Player " + ChatColor.YELLOW + target1.getName() + ChatColor.RED + " is offline.");
                return true;
            } else if (target2 == null) {
                p.sendMessage(ChatColor.RED + "Player " + ChatColor.YELLOW + target2.getName() + ChatColor.RED + " is offline.");
                return true;
            }

            target1.teleport(target2.getLocation());
            target1.sendMessage(ChatColor.YELLOW + p.getDisplayName() + ChatColor.GRAY + " has teleported you to " + ChatColor.GOLD + target2.getDisplayName());

            p.sendMessage(ChatColor.GREEN + "Teleported " + ChatColor.YELLOW + target1.getDisplayName() + ChatColor.GREEN + " to " + ChatColor.GOLD + target2.getDisplayName());
        } else {
            p.sendMessage(ChatColor.RED + "Invalid Usage.");
            return false;
        }
        return true;
    }
}
