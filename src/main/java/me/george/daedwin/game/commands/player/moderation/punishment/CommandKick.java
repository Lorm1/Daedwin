package me.george.daedwin.game.commands.player.moderation.punishment;

import me.george.daedwin.manager.LogoutManager;
import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandKick implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            DaedwinPlayer daedwinPlayer = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

            if (!daedwinPlayer.isStaff()) return true;

            if (args.length < 1) {
                p.sendMessage(ChatColor.RED + "Invalid Usage");
                return false;
            } else if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (!target.isOnline() || target == null) {
                    p.sendMessage(ChatColor.RED + "That player is not online.");
                    return true;
                }

                DaedwinPlayer daedwinTarget = DaedwinPlayer.getDaedwinPlayers().get(target.getUniqueId());

                LogoutManager.handleLogout(daedwinTarget, "You have been Kicked.");

                for (Player pl : Bukkit.getOnlinePlayers()) {
                    DaedwinPlayer pla = DaedwinPlayer.getDaedwinPlayers().get(pl.getUniqueId());
                    if (pla.isStaff()) {
                        pla.getPlayer().sendMessage(ChatColor.YELLOW + p.getName() + ChatColor.RED + " has kicked the player " + ChatColor.YELLOW + target.getName()
                                + ChatColor.GOLD.toString() + ChatColor.UNDERLINE + " Reason"
                                + ChatColor.GOLD + ": " + ChatColor.WHITE + "Unspecified");
                    }
                }
            } else if (args.length > 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    p.sendMessage(ChatColor.RED + "That player is not online.");
                    return true;
                }

                String reason = "";

                for (int i = 2; i < args.length; i++) {
                    reason += args[i] + " ";
                }

                DaedwinPlayer daedwinTarget = DaedwinPlayer.getDaedwinPlayers().get(target.getUniqueId());

                LogoutManager.handleLogout(daedwinTarget, reason);

                for (Player pl : Bukkit.getOnlinePlayers()) {
                    DaedwinPlayer pla = DaedwinPlayer.getDaedwinPlayers().get(pl.getUniqueId());
                    if (pla.isStaff()) {
                        pla.getPlayer().sendMessage(ChatColor.YELLOW + p.getName() + ChatColor.RED + " has kicked the player " + ChatColor.YELLOW + target.getName()
                                + ChatColor.GOLD.toString() + ChatColor.UNDERLINE + " Reason"
                                + ChatColor.GOLD + ": " + ChatColor.WHITE + reason);
                    }
                }
            } else {
                p.sendMessage(ChatColor.RED + "Invalid Usage.");
                return false;
            }
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Invalid Usage");
                return false;
            } else if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "That player is not online.");
                    return true;
                }

                DaedwinPlayer daedwinTarget = DaedwinPlayer.getDaedwinPlayers().get(target.getUniqueId());

                LogoutManager.handleLogout(daedwinTarget, "You have been Kicked.");

                for (Player pl : Bukkit.getOnlinePlayers()) {
                    DaedwinPlayer pla = DaedwinPlayer.getDaedwinPlayers().get(pl.getUniqueId());
                    if (pla.isStaff()) {
                        pla.getPlayer().sendMessage(ChatColor.DARK_RED + "CONSOLE" + ChatColor.RED + " has kicked the player " + ChatColor.YELLOW + target.getName()
                                + ChatColor.GOLD.toString() + ChatColor.UNDERLINE + " Reason"
                                + ChatColor.GOLD + ": " + ChatColor.WHITE + "Unspecified");
                    }
                }
            } else if (args.length > 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "That player is not online.");
                    return true;
                }

                String reason = "";

                for (int i = 2; i < args.length; i++) {
                    reason += args[i] + " ";
                }

                DaedwinPlayer daedwinTarget = DaedwinPlayer.getDaedwinPlayers().get(target.getUniqueId());

                LogoutManager.handleLogout(daedwinTarget, reason);

                for (Player pl : Bukkit.getOnlinePlayers()) {
                    DaedwinPlayer pla = DaedwinPlayer.getDaedwinPlayers().get(pl.getUniqueId());
                    if (pla.isStaff()) {
                        pla.getPlayer().sendMessage(ChatColor.DARK_RED + "CONSOLE" + ChatColor.RED + " has kicked the player " + ChatColor.YELLOW + target.getName()
                                + ChatColor.GOLD.toString() + ChatColor.UNDERLINE + " Reason"
                                + ChatColor.GOLD + ": " + ChatColor.WHITE + reason);
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid Usage.");
                return false;
            }
        }
        return true;
    }
}
