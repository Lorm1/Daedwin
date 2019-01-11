package me.george.daedwin.game.commands.essentials;

import me.george.daedwin.game.player.DaedwinPlayer;
import me.george.daedwin.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class CommandClearMobs implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player p = (Player) sender;
        DaedwinPlayer daedwinPlayer = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        if (!daedwinPlayer.isAdmin()) return true;

        if (args.length > 2) {
            p.sendMessage(ChatColor.RED + "Invalid Usage.");
            return false;
        }

        int radius = 50;

        EntityType entityType;
        int counter = 0;

        if (args.length == 0) { // /clearmobs
            for (Entity entity : Utils.getNearbyEntities(p.getLocation(), radius)) {
                if (!entity.equals(EntityType.PLAYER)) {
                    entity.remove();
                    counter++;
                }
            }

            p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "Cleared " + ChatColor.RED.toString() + ChatColor.BOLD + counter
                    + ChatColor.GREEN.toString() + ChatColor.BOLD + " Entities in a " + ChatColor.RED.toString() + ChatColor.BOLD
                    + radius + ChatColor.GREEN.toString() + ChatColor.BOLD + " block radius.");
        } else if (args.length == 1) { // clearmobs <radius>
            try {
                try {
                    radius = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    p.sendMessage(ChatColor.RED + "Invalid Radius.");
                    return false;
                }

                for (Entity entity : Utils.getNearbyEntities(p.getLocation(), radius)) {
                    if (!entity.equals(EntityType.PLAYER)) {
                        entity.remove();
                        counter++;
                    }
                }

                p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "Cleared " + ChatColor.RED.toString() + ChatColor.BOLD + counter
                        + ChatColor.GREEN.toString() + ChatColor.BOLD + " Entities in a " + ChatColor.RED.toString() + ChatColor.BOLD
                        + radius + ChatColor.GREEN.toString() + ChatColor.BOLD + " block radius.");
            } catch (IllegalArgumentException e) {
                p.sendMessage(ChatColor.RED + "Invalid Entity Type");
                return false;
            }
        } else if (args.length == 2) { // clearmobs <radius> <entity>
            String mobName = args[0];
            try {
                radius = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                p.sendMessage(ChatColor.RED + "Invalid Radius.");
                return false;
            }
            try {
                entityType = EntityType.valueOf(mobName.toUpperCase());

                for (Entity entity : Utils.getNearbyEntities(p.getLocation(), radius)) {
                    if (!entity.equals(EntityType.PLAYER)) {
                        if (entityType.equals(entity)) {
                            entity.remove();
                            counter++;
                        }
                    }
                }

                p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "Cleared " + ChatColor.RED.toString() + ChatColor.BOLD + counter
                        + ChatColor.GREEN.toString() + ChatColor.BOLD + " Entities in a " + ChatColor.RED.toString() + ChatColor.BOLD
                        + radius + ChatColor.GREEN.toString() + ChatColor.BOLD + " block radius.");
            } catch (IllegalArgumentException e) {
                p.sendMessage(ChatColor.RED + "Invalid Entity Type.");
                return false;
            }
        }
        return true;
    }
}
