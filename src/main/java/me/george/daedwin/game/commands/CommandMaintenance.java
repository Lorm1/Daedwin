package me.george.daedwin.game.commands;

import me.george.daedwin.Constants;
import me.george.daedwin.Daedwin;
import me.george.daedwin.Setup;
import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandMaintenance implements CommandExecutor {
    // maintenance on|off|add|remove|clear <player>(optional)

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (Constants.ADMINS.contains(sender.getName()) || sender.isOp() || sender instanceof ConsoleCommandSender) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Invalid Usage");
                return false;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("on")) {
                    if (!Setup.MAINTENANCE_MODE) {
                        sender.sendMessage(ChatColor.GOLD + "Maintenance Mode - " + ChatColor.GREEN.toString() + ChatColor.BOLD + "ENABLED");

                        Setup.MAINTENANCE_MODE = !Setup.MAINTENANCE_MODE; // on

                        for (Player pl : Bukkit.getOnlinePlayers()) {
                            DaedwinPlayer pla = DaedwinPlayer.getDaedwinPlayers().get(pl.getUniqueId());

                            if (!Daedwin.getInstance().getFileManager().getWhitelistedPlayers().contains(pla.getPlayer().getName().toLowerCase())) {
                                pla.getPlayer().kickPlayer(ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "Myths of Daedwin\n\n\n"
                                        + ChatColor.RED.toString() + ChatColor.UNDERLINE + "Maintenance Mode" + ChatColor.RED + "" + " has been Enabled.\n\n\n"
                                        + ChatColor.GRAY + "Find out more at: " + ChatColor.BLUE.toString() + ChatColor.UNDERLINE + Constants.SITE_NAME);
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Maintenance Mode is already enabled.");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("off")) {
                    if (Setup.MAINTENANCE_MODE) {
                        sender.sendMessage(ChatColor.GOLD + "Maintenance Mode - " + ChatColor.RED.toString() + ChatColor.BOLD + "DISABLED");

                        Setup.MAINTENANCE_MODE = !Setup.MAINTENANCE_MODE; // off
                    } else {
                        sender.sendMessage(ChatColor.RED + "Maintenance Mode is not enabled.");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("add")) {
                    sender.sendMessage(ChatColor.RED + "Too few arguments.");
                    return false;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    sender.sendMessage(ChatColor.RED + "Too few arguments.");
                    return false;
                } else if (args[0].equalsIgnoreCase("clear")) {
                    sender.sendMessage(ChatColor.GREEN + "Cleared the whitelist.");

                    Daedwin.getInstance().getFileManager().getWhitelistedPlayers().clear();
                } else if (args[0].equalsIgnoreCase("show")) {
                    sender.sendMessage(ChatColor.BLUE.toString() + ChatColor.UNDERLINE + "Whitelisted Players: \n");
                    sender.sendMessage(ChatColor.GRAY + Arrays.toString(Daedwin.getInstance().getFileManager().getWhitelistedPlayers().toArray()));
                }
            } else if (args.length == 2) {
                String playerName = args[1].toLowerCase();
                if (args[0].equalsIgnoreCase("add")) {
                    if (!Daedwin.getInstance().getFileManager().getWhitelistedPlayers().contains(playerName.toLowerCase())) {
                        Daedwin.getInstance().getFileManager().addToWhitelist(playerName.toLowerCase());
                        sender.sendMessage(ChatColor.GREEN + "Added player " + ChatColor.YELLOW + playerName + ChatColor.GREEN + " to the whitelist.");
                    } else {
                        sender.sendMessage(ChatColor.RED + "That player is already whitelisted.");
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (Daedwin.getInstance().getFileManager().getWhitelistedPlayers().contains(playerName.toLowerCase())) {
                        Daedwin.getInstance().getFileManager().removeFromWhitelist(playerName.toLowerCase());
                        sender.sendMessage(ChatColor.GREEN + "Removed " + ChatColor.YELLOW + playerName + ChatColor.GREEN + " from the whitelist.");
                    } else {
                        sender.sendMessage(ChatColor.RED + "That player is not whitelisted.");
                    }
                } else if (args[0].equalsIgnoreCase("check")) {
                    if (Daedwin.getInstance().getFileManager().getWhitelistedPlayers().contains(playerName.toLowerCase())) {
                        sender.sendMessage(ChatColor.GREEN + "This player is whitelisted.");
                    } else {
                        sender.sendMessage(ChatColor.RED + "This player is not whitelisted.");
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid Usage.");
                return false;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Insufficient Permission.");
        }
        return true;
    }
}
