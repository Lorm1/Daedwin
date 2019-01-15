package me.george.daedwin.game.commands.player.moderation;

import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandTeleport implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

            if (!player.isAdmin()) return true;

            if (cmd.getLabel().equalsIgnoreCase("teleportloc") || cmd.getLabel().equalsIgnoreCase("tploc") || cmd.getLabel().equalsIgnoreCase("tplocation")) {
                if (args.length != 3) {
                    p.sendMessage(ChatColor.RED + "Invalid Coordinates.");
                    p.sendMessage(ChatColor.RED + "/tploc <x> <y> <z>");
                    return true;
                }

                Location location;
                try {
                    location = new Location(p.getWorld(), Double.valueOf(args[0]), Double.valueOf(args[1]), Double.valueOf(args[2]));
                    p.teleport(location);
                } catch (Exception e) {
                    p.sendMessage(ChatColor.RED + "Invalid Coordinates.");
                    p.sendMessage(ChatColor.RED + "/tploc <x> <y> <z>");
                    return true;
                }
            }

            if (args.length == 1) { // /tp <player>
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
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
                }

                if (target2 == null) {
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
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) { // /tp <player>
                sender.sendMessage(ChatColor.RED + "The console can only teleport one player to another.");
                return false;
            } else if (args.length == 2) { // /tp <player1> <player2>
                Player target1 = Bukkit.getPlayer(args[0]);
                Player target2 = Bukkit.getPlayer(args[1]);

                if (target1 == null) {
                    sender.sendMessage(ChatColor.RED + "Player " + ChatColor.YELLOW + target1.getName() + ChatColor.RED + " is offline.");
                    return true;
                }

                if (target2 == null) {
                    sender.sendMessage(ChatColor.RED + "Player " + ChatColor.YELLOW + target2.getName() + ChatColor.RED + " is offline.");
                    return true;
                }

                target1.teleport(target2.getLocation());
                target1.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "CONSOLE" + ChatColor.GRAY + " has teleported you to " + ChatColor.GOLD + target2.getDisplayName());

                sender.sendMessage(ChatColor.GREEN + "Teleported " + ChatColor.YELLOW + target1.getDisplayName() + ChatColor.GREEN + " to " + ChatColor.GOLD + target2.getDisplayName());
            }
        }
        return true;
    }

}
