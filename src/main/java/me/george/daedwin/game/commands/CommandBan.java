package me.george.daedwin.game.commands;

import me.george.daedwin.Daedwin;
import me.george.daedwin.database.DatabaseAPI;
import me.george.daedwin.game.player.DaedwinPlayer;
import me.george.daedwin.utils.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandBan implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

            if (!player.isAdmin()) return true;

            if (args.length <= 1) {
                p.sendMessage(ChatColor.RED + "Invalid Usage.");
                return false;
            }

            String targetName = args[0];

            if (!DatabaseAPI.playerExists(targetName)) {
                p.sendMessage(ChatColor.RED + "That player has never played before.");
                return true;
            }

            UUID targetUUID = DatabaseAPI.getPlayerUUID(targetName);

            if (Daedwin.getInstance().getBanManager().isBanned(targetUUID)) {
                p.sendMessage(ChatColor.RED + "That player is already banned!");
                return true;
            }

            String reason = "";

            for (int i = 2; i < args.length; i++) {
                reason += args[i] + " ";
            }

            if (args[1].equalsIgnoreCase("perm") || args[1] == null) {
                Daedwin.getInstance().getBanManager().ban(targetUUID, -1, reason);
                p.sendMessage(ChatColor.RED + "Banned player " + ChatColor.YELLOW + targetName
                        + ChatColor.RED.toString() + ChatColor.UNDERLINE + " PERMANENTLY."
                        + ChatColor.AQUA + " Reason: " + ChatColor.WHITE + (reason.equals("") ? "Not Specified" : reason));
                return true;
            }

            if (!args[1].contains(":")) {
                p.sendMessage(ChatColor.RED + "Invalid Usage.");
                return false;
            }

            int duration = 0;
            try {
                duration = Integer.parseInt(args[1].split(":")[0]);
            } catch (NumberFormatException e) {
                p.sendMessage(ChatColor.RED + "Invalid Number.");
                return false;
            }

            if (!TimeUnit.existFromShortcut(args[1].split(":")[1])) {
                p.sendMessage(ChatColor.RED + "Invalid Input.");
                for (TimeUnit unit : TimeUnit.values()) {
                    p.sendMessage(ChatColor.GRAY + unit.getName() + ChatColor.WHITE + " : " + ChatColor.YELLOW + unit.getShortcut());
                }
                return true;
            }

            TimeUnit unit = TimeUnit.getFromShortcut(args[1].split(":")[1]);
            long banTime = unit.getToSecond() * duration;

            String finalReason = reason;
            Bukkit.getScheduler().runTaskAsynchronously(Daedwin.getInstance(), () -> Daedwin.getInstance().getBanManager().ban(targetUUID, banTime, finalReason));
            p.sendMessage(ChatColor.RED + "Banned player " + ChatColor.YELLOW + targetName
                    + ChatColor.RED.toString() + ChatColor.GOLD + "Duration: "
                    + ChatColor.RED + banTime + ChatColor.AQUA + "Reason: "
                    + ChatColor.WHITE + (reason.equals("") ? "Not Specified" : reason));
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length == 3) {
                sender.sendMessage(ChatColor.RED + "Invalid Usage.");
                return false;
            }

            String targetName = args[0];

            if (!DatabaseAPI.playerExists(targetName)) {
                sender.sendMessage(ChatColor.RED + "That player has never played before.");
                return true;
            }

            UUID targetUUID = DatabaseAPI.getPlayerUUID(targetName);

            if (Daedwin.getInstance().getBanManager().isBanned(targetUUID)) {
                sender.sendMessage(ChatColor.RED + "That player is already banned!");
                return true;
            }

            String reason = "";

            for (int i = 2; i < args.length; i++) {
                reason += args[i] + " ";
            }

            if (args[1].equalsIgnoreCase("perm")) {
                String finalReason1 = reason;
                Bukkit.getScheduler().runTaskAsynchronously(Daedwin.getInstance(), () -> Daedwin.getInstance().getBanManager().ban(targetUUID, -1, finalReason1));
                sender.sendMessage(ChatColor.RED + "Banned player " + ChatColor.YELLOW + targetName
                        + ChatColor.RED.toString() + ChatColor.UNDERLINE + "PERMANENTLY. "
                        + ChatColor.AQUA + "Reason: " + ChatColor.WHITE + reason);
                return true;
            }

            if (!args[1].contains(":")) {
                sender.sendMessage(ChatColor.RED + "Invalid Usage.");
                return false;
            }

            int duration = 0;
            try {
                duration = Integer.parseInt(args[1].split(":")[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid Number.");
                return false;
            }

            if (!TimeUnit.existFromShortcut(args[1].split(":")[1])) {
                sender.sendMessage(ChatColor.RED + "Invalid Input.");
                for (TimeUnit unit : TimeUnit.values()) {
                    sender.sendMessage(ChatColor.GRAY + unit.getName() + ChatColor.WHITE + " : " + ChatColor.YELLOW + unit.getShortcut());
                }
                return true;
            }

            TimeUnit unit = TimeUnit.getFromShortcut(args[1].split(":")[1]);
            long banTime = unit.getToSecond() * duration;

            String finalReason = reason;
            Bukkit.getScheduler().runTaskAsynchronously(Daedwin.getInstance(), () -> Daedwin.getInstance().getBanManager().ban(targetUUID, banTime, finalReason));
            sender.sendMessage(ChatColor.RED + "Banned player " + ChatColor.YELLOW + targetName
                    + ChatColor.RED.toString() + ChatColor.GOLD + "Duration: "
                    + ChatColor.RED + banTime + ChatColor.AQUA + "Reason: "
                    + ChatColor.WHITE + reason);
        }
        return true;
    }
}
