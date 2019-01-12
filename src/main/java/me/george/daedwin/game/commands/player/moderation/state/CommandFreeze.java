package me.george.daedwin.game.commands.player.moderation.state;

import me.george.daedwin.Daedwin;
import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandFreeze implements CommandExecutor {
    public static List<Player> frozen = new ArrayList<Player>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //if (cmd.getName().equalsIgnoreCase("freeze")) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

            if (player.isStaff()) {
                if (args.length < 1) {
                    p.sendMessage(ChatColor.RED + "Too short arguments!");
                    return false;
                }

                if (args.length == 1) {
                    Player t = Bukkit.getServer().getPlayer(args[0]);

                    if (t == null) {
                        p.sendMessage(ChatColor.RED + "That player is not online!");
                        return true;
                    }

                    DaedwinPlayer target = DaedwinPlayer.getDaedwinPlayers().get(t.getUniqueId());

                    if (!frozen.contains(t)) {
                        if (target.isStaff()) {
                            p.sendMessage(ChatColor.RED + "You cannot freeze this person.");
                            return true;
                        }

                        frozen.add(t);
                        p.sendMessage(ChatColor.BLUE + "Froze player " + ChatColor.YELLOW + t.getName());

                        t.sendMessage(ChatColor.BLUE + "You have been frozen.");
                    } else {
                        frozen.remove(t);
                        p.sendMessage(ChatColor.BLUE + "Unfroze player " + ChatColor.YELLOW + t.getName());

                        t.sendMessage(ChatColor.BLUE + "You have been unfrozen.");
                    }
                }

                if (args.length == 2) {
                    Player t = Bukkit.getServer().getPlayer(args[0]);

                    if (t == null) {
                        p.sendMessage(ChatColor.RED + "That player is not online!");
                        return true;
                    }

                    DaedwinPlayer target = DaedwinPlayer.getDaedwinPlayers().get(t.getUniqueId());

                    int time = Integer.parseInt(null);
                    try {
                        time = Integer.parseInt(args[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (!frozen.contains(t)) {
                        if (target.isStaff()) {
                            p.sendMessage(ChatColor.RED + "You cannot freeze this person.");
                            return true;
                        }

                        frozen.add(t);

                        p.sendMessage(ChatColor.RED + "Froze player " + ChatColor.YELLOW + t.getName() + ChatColor.RED + " for " + ChatColor.GOLD + time + ChatColor.RED + " seconds.");
                        t.sendMessage(ChatColor.BLUE + "You have been frozen for " + ChatColor.GOLD + time + ChatColor.BLUE + " seconds.");

                        int task = Bukkit.getScheduler().scheduleSyncDelayedTask(Daedwin.getInstance(), new Runnable() {

                            @Override
                            public void run() {
                                if (frozen.contains(t)) {
                                    frozen.remove(t);
                                    t.sendMessage(ChatColor.BLUE + "You are no longer frozen.");
                                }
                            }

                        }, time * 20);
                    } else {
                        frozen.remove(t);

                        p.sendMessage(ChatColor.RED + "Unfroze player " + ChatColor.YELLOW + t.getName());
                        t.sendMessage(ChatColor.BLUE + "You have been unfrozen.");
                    }
                }
            } else {
                p.sendMessage(ChatColor.RED + "You do not have permission!");
                return true;
            }
        }
        // }
        return true;
    }
}
