package me.george.daedwin.game.commands;

import me.george.daedwin.Constants;
import me.george.daedwin.Daedwin;
import me.george.daedwin.database.DatabaseAPI;
import me.george.daedwin.game.rank.Rank;
import me.george.daedwin.game.player.DaedwinPlayer;
import me.george.daedwin.game.punishment.TimeUnit;
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

            if (args.length < 1) {
                p.sendMessage(ChatColor.RED + "Invalid Usage.");
                return false;
            }

            String targetName = args[0];

            if (!DatabaseAPI.playerExists(targetName)) {
                p.sendMessage(ChatColor.RED + "That player has never played before.");
                return true;
            }

            UUID targetUUID = DatabaseAPI.getPlayerUUID(targetName);

            if (Daedwin.getInstance().getPunishmentManager().getBanManager().isBanned(targetUUID)) {
                p.sendMessage(ChatColor.RED + "That player is already banned.");
                return true;
            }

            if (Constants.ADMINS.contains(targetName) || DatabaseAPI.getPlayerRank(targetUUID).equals(Rank.ADMIN)) {
                p.sendMessage(ChatColor.RED + "You cannot ban this player.");
                return true;
            }

            boolean hasDuration = true;

            if (args.length == 1) { // ban <player>
                hasDuration = false; // perma

                Daedwin.getInstance().getPunishmentManager().getBanManager().ban(targetUUID, -1, "Unspecified");
                p.sendMessage(ChatColor.RED + "Banned player " + ChatColor.YELLOW + targetName + " "
                        + ChatColor.RED.toString() + ChatColor.UNDERLINE + "PERMANENTLY."
                        + ChatColor.AQUA + " Reason: " + ChatColor.DARK_RED + "Unspecified");
                return true;
            }

            String reason = "";

            for (int i = 2; i < args.length; i++) {
                reason += args[i] + " ";
            }

            if (args[1].equalsIgnoreCase("perm")) {
                hasDuration = false;

                Daedwin.getInstance().getPunishmentManager().getBanManager().ban(targetUUID, -1, reason.equals("") ? "Unspecified" : reason);
                p.sendMessage(ChatColor.RED + "Banned player " + ChatColor.YELLOW + targetName + " "
                        + ChatColor.RED.toString() + ChatColor.UNDERLINE + "PERMANENTLY."
                        + ChatColor.AQUA + " Reason: " + ChatColor.DARK_RED + (reason.equals("") ? "Unspecified" : reason));
                return true;
            }

            if (!args[1].contains(":") && hasDuration) {
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

            Daedwin.getInstance().getPunishmentManager().getBanManager().ban(targetUUID, banTime, reason.equals("") ? "Unspecified" : reason);
            p.sendMessage(ChatColor.RED + "Banned player " + ChatColor.YELLOW + targetName
                    + ChatColor.RED.toString() + ChatColor.GRAY + " Duration: "
                    + ChatColor.RED + banTime + ChatColor.AQUA + " Reason: "
                    + ChatColor.WHITE + (reason.equals("") ? "Unspecified" : reason));
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Invalid Usage.");
                return false;
            }

            String targetName = args[0];

            if (!DatabaseAPI.playerExists(targetName)) {
                sender.sendMessage(ChatColor.RED + "That player has never played before.");
                return true;
            }

            UUID targetUUID = DatabaseAPI.getPlayerUUID(targetName);

            if (Daedwin.getInstance().getPunishmentManager().getBanManager().isBanned(targetUUID)) {
                sender.sendMessage(ChatColor.RED + "That player is already banned!");
                return true;
            }

            boolean hasDuration = true;

            if (args.length == 1) { // ban <player>
                hasDuration = false; // perma

                Daedwin.getInstance().getPunishmentManager().getBanManager().ban(targetUUID, -1, "Unspecified");
                sender.sendMessage(ChatColor.RED + "Banned player " + ChatColor.YELLOW + targetName + " "
                        + ChatColor.RED.toString() + ChatColor.UNDERLINE + "PERMANENTLY."
                        + ChatColor.AQUA + " Reason: " + ChatColor.DARK_RED + "Unspecified");
                return true;
            }

            String reason = "";

            for (int i = 2; i < args.length; i++) {
                reason += args[i] + " ";
            }

            if (args[1].equalsIgnoreCase("perm")) {
                hasDuration = false;

                Daedwin.getInstance().getPunishmentManager().getBanManager().ban(targetUUID, -1, reason.equals("") ? "Unspecified" : reason);
                sender.sendMessage(ChatColor.RED + "Banned player " + ChatColor.YELLOW + targetName + " "
                        + ChatColor.RED.toString() + ChatColor.UNDERLINE + "PERMANENTLY."
                        + ChatColor.AQUA + " Reason: " + ChatColor.DARK_RED + (reason.equals("") ? "Unspecified" : reason));
                return true;
            }

            if (!args[1].contains(":") && hasDuration) {
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

            Daedwin.getInstance().getPunishmentManager().getBanManager().ban(targetUUID, banTime, reason.equals("") ? "Unspecified" : reason);
            sender.sendMessage(ChatColor.RED + "Banned player " + ChatColor.YELLOW + targetName
                    + ChatColor.RED.toString() + ChatColor.GRAY + " Duration: "
                    + ChatColor.RED + banTime + ChatColor.AQUA + " Reason: "
                    + ChatColor.DARK_RED + (reason.equals("") ? "Unspecified" : reason));
        }
        return true;
    }
}
