package me.george.daedwin.game.commands.player.moderation.mode;

import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGameMode implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        if (!player.isAdmin()) return true;

        if (args.length == 0) { // /gm
            p.sendMessage(ChatColor.RED + "Invalid Usage.");
            return false;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("creative") || args[0].equals("1")) {
                if (!p.getGameMode().equals(GameMode.CREATIVE)) {
                    p.setGameMode(GameMode.CREATIVE);
                    p.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "GAMEMODE - " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "CREATIVE");
                } else {
                    p.sendMessage(ChatColor.RED + "You are already in Creative Mode.");
                }
            } else if (args[0].equalsIgnoreCase("survival") || args[0].equals("0")) {
                if (!p.getGameMode().equals(GameMode.SURVIVAL)) {
                    p.setGameMode(GameMode.SURVIVAL);
                    p.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "GAMEMODE - " + ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "SURVIVAL");
                } else {
                    p.sendMessage(ChatColor.RED + "You are already in Survival Mode.");
                }
            } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("spec") || args[0].equals("2")) {
                if (!p.getGameMode().equals(GameMode.SURVIVAL)) {
                    p.setGameMode(GameMode.SPECTATOR);
                    p.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "GAMEMODE - " + ChatColor.YELLOW.toString() + ChatColor.BOLD + "SPECTATOR");
                } else {
                    p.sendMessage(ChatColor.RED + "You are already in Spectator Mode.");
                }
            } else {
                p.sendMessage(ChatColor.RED + "Invalid GameMode.");
                return false;
            }
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                p.sendMessage(ChatColor.RED + "That player is offline.");
                return true;
            }

            if (args[1].equalsIgnoreCase("creative") || args[1].equals("1")) {
                if (!target.getGameMode().equals(GameMode.CREATIVE)) {
                    target.setGameMode(GameMode.CREATIVE);
                    target.sendMessage(ChatColor.AQUA + p.getName() + ChatColor.GREEN + " has set your Gamemode to " + ChatColor.LIGHT_PURPLE + "CREATIVE");
                    p.sendMessage(ChatColor.AQUA + "Set " + ChatColor.YELLOW + target.getName() + ChatColor.AQUA + "'s GAMEMODE TO - " + ChatColor.LIGHT_PURPLE + "CREATIVE");
                } else {
                    p.sendMessage(ChatColor.RED + "That player is already in Creative Mode.");
                }
            } else if (args[1].equalsIgnoreCase("survival") || args[1].equals("0")) {
                if (!target.getGameMode().equals(GameMode.SURVIVAL)) {
                    target.setGameMode(GameMode.SURVIVAL);
                    target.sendMessage(ChatColor.AQUA + p.getName() + ChatColor.GREEN + " has set your Gamemode to " + ChatColor.DARK_GREEN + "SURVIVAL");
                    p.sendMessage(ChatColor.AQUA + "Set " + ChatColor.YELLOW + target.getName() + ChatColor.AQUA + "'s GAMEMODE TO - " + ChatColor.DARK_GREEN + "SURVIVAL");
                } else {
                    p.sendMessage(ChatColor.RED + "That player is already in Survival Mode.");
                }
            } else if (args[1].equalsIgnoreCase("spectator") || args[1].equalsIgnoreCase("spec") || args[0].equals("2")) {
                if (!target.getGameMode().equals(GameMode.SPECTATOR)) {
                    target.setGameMode(GameMode.SPECTATOR);
                    target.sendMessage(ChatColor.AQUA + p.getName() + ChatColor.GREEN + " has set your Gamemode to " + ChatColor.YELLOW + "SPECTATOR");
                    p.sendMessage(ChatColor.AQUA + "Set " + ChatColor.YELLOW + target.getName() + ChatColor.AQUA + "'s GAMEMODE TO - " + ChatColor.YELLOW + "SPECTATOR");
                } else {
                    p.sendMessage(ChatColor.RED + "That player is already in Spectator Mode.");
                }
            } else {
                p.sendMessage(ChatColor.RED + "Invalid GameMode.");
                return false;
            }
        } else {
            p.sendMessage(ChatColor.RED + "Invalid Usage.");
            return false;
        }
        return true;
    }
}
