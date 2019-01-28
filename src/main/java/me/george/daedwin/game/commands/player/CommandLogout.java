package me.george.daedwin.game.commands.player;

import me.george.daedwin.Daedwin;
import me.george.daedwin.manager.LogoutManager;
import me.george.daedwin.utils.Countdown;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLogout implements CommandExecutor {

    boolean isSummoning = false;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            Location loc = p.getLocation();

            new Countdown(5, Daedwin.getInstance()) {

                @Override
                public void count(int current) {
                    isSummoning = true;

                    Location location = p.getLocation();

                    if (loc.getBlock().getX() != location.getBlock().getX()) {
                        isSummoning = false;

                        this.task.cancel();

                        p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Logout " + ChatColor.RED + "" + ChatColor.UNDERLINE + "Cancelled.");
                        p.sendActionBar(ChatColor.RED + "Logout Cancelled.");

                        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 5F, 5F);
                    }

                    if (isSummoning) {
                        p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Logging out " + ChatColor.RED + "in " + ChatColor.RED.toString() + ChatColor.BOLD + current + ChatColor.RED + " seconds.");

                        p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 5F, 5F);

                        p.sendTitle("", ChatColor.RED + "Logging out in " + current + "s...", 20 * 1, 20 * 2, 20 * 1);
                        p.sendActionBar(ChatColor.RED + "Do not move!");

                        if (current == 0) {
                            p.sendMessage(ChatColor.GREEN + "Logging out...");
                            LogoutManager.handleLogout(p, ChatColor.GREEN + "You have " + ChatColor.GREEN + ChatColor.BOLD + "Logged Out.");
                        }
                    }
                }
            }.start();
        }
        return true;
    }
}
