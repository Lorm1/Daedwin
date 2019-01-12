package me.george.daedwin.game.commands.player;

import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandMessage implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

            if (args.length < 2) {
                p.sendMessage(ChatColor.RED + "Invalid Usage.");
                return false;
            }

            // /message <player> <message>
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                p.sendMessage(ChatColor.RED + "That player is offline.");
                return true;
            }

            DaedwinPlayer t = DaedwinPlayer.getDaedwinPlayers().get(target.getUniqueId());

            StringBuilder str = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                str.append(args[i] + " ");
            }

            p.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "Me"+ ChatColor.BLUE + " -> " + t.getRank().getPrefix() + ChatColor.RESET + " " + (t.getHasNickname() ? t.getNickname() : ChatColor.GRAY + target.getDisplayName()) + ": " + ChatColor.translateAlternateColorCodes('&', str.toString().trim()));

            target.sendMessage(player.getRank().getPrefix() + ChatColor.RESET + " " + (player.getHasNickname() ? player.getNickname() : ChatColor.GRAY + p.getDisplayName()) + ChatColor.BLUE + " -> " + ChatColor.GRAY.toString() + ChatColor.ITALIC + "me: " + ChatColor.translateAlternateColorCodes('&', str.toString().trim()));
            target.playSound(target.getLocation(), Sound.ENTITY_CHICKEN_EGG, 5, 5);
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Invalid Usage.");
                return false;
            }

            // /message <player> <message>
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "That player is offline.");
                return true;
            }

            DaedwinPlayer t = DaedwinPlayer.getDaedwinPlayers().get(target.getUniqueId());

            StringBuilder str = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                str.append(args[i] + " ");
            }

            sender.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "CONSOLE"+ ChatColor.BLUE + " -> " + t.getRank().getPrefix() + ChatColor.RESET + " " + (t.getHasNickname() ? t.getNickname() : ChatColor.GRAY + target.getDisplayName()) + ": "+ ChatColor.translateAlternateColorCodes('&', str.toString().trim()));

            target.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "CONSOLE" + ChatColor.BLUE + " -> " + ChatColor.GRAY.toString() + ChatColor.ITALIC + "me: " + ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', str.toString().trim()));
            target.playSound(target.getLocation(), Sound.ENTITY_CHICKEN_EGG, 5, 5);
        }
        return true;
    }
}
