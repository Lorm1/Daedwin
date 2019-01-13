package me.george.daedwin.game.commands.player;

import me.george.daedwin.Daedwin;
import me.george.daedwin.manager.LogoutManager;
import me.george.daedwin.game.player.DaedwinPlayer;
import me.george.daedwin.utils.cooldown.Countdown;
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
        /*if (cmd.getName().equalsIgnoreCase("logout")) {*/
        if (sender instanceof Player) {
            Player p = (Player) sender;
            DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

            Location loc = p.getLocation(); // starting location

            new Countdown(5, Daedwin.getInstance()) {

                @Override
                public void count(int current) {
                    isSummoning = true; // logging out in progress

                    Location location = p.getLocation(); // ? new location ?

                    // if (!isSummoning) {
//                                this.task.cancel();
//
//                                TTABUtils.sendActionBar(player, ChatColor.RED + "Logout Process " + ChatColor.RED + "" + ChatColor.UNDERLINE + "Cancelled.");
//
//                                player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Logout " + ChatColor.RED + "" + ChatColor.UNDERLINE + "CANCELED.");
//
//                                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 5F, 5F);
                    // }

                    if (/*loc.getBlock().getZ() != location.getBlock().getZ() || */ loc.getBlock().getX() != location.getBlock().getX()) { // check if the player has moved, and if so, cancel the logout process.
                        isSummoning = false;

                        this.task.cancel();

                        p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Logout " + ChatColor.RED + "" + ChatColor.UNDERLINE + "CANCELED.");

                        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 5F, 5F);
                    }

                    if (isSummoning) { // do the process normally
                        p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Logging out " + ChatColor.RED + "in " + ChatColor.RED.toString() + ChatColor.BOLD + current + ChatColor.RED + " seconds.");

                        p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 5F, 5F);

                        if (current == 0) { // disconnect the player after saving his data
                            p.sendMessage(ChatColor.GREEN + "Logging out...");
                            LogoutManager.handleLogout(DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId()), ChatColor.GREEN + "You have " + ChatColor.GREEN + ChatColor.BOLD + "Logged Out.");
                        }
                    }
                }
            }.start();
        }
        return true;
    }
    /*}*/
}
