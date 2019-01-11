package me.george.daedwin.game.commands.essentials;

import me.george.daedwin.game.player.DaedwinPlayer;
import me.george.daedwin.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

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

        int radius = 25;

        EntityType entityType;
        int counter = 0;

        if (args.length >= 1) { // clearmobs <radius> <entity>(maybe)
            try {
                radius = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                p.sendMessage(ChatColor.RED + "Invalid Radius.");
                return false;
            }

            if (radius > 500) {
                p.sendMessage(ChatColor.RED + "Max Radius: 500");
                return true;
            }
        }

        if (args.length < 2) { // /clearmobs and /clearmobs <radius>
            for (Entity en : Utils.getNearbyEntities(p.getLocation(), radius)) {
                if (!(en instanceof Player) && !(en == null) && !(en instanceof NPC) && !(en instanceof Villager)) {
                    en.remove();
                    counter++;
                }
            }
        }

        // clearmobs <radius> <entity>
        if (args.length  == 2) {
            try {
                entityType = EntityType.valueOf(args[1].toUpperCase());
            } catch (Exception e) {
                p.sendMessage(ChatColor.RED + "Invalid Entity.");
                return false;
            }

            for (Entity en : Utils.getNearbyEntities(p.getLocation(), radius)) {
                if (!(en instanceof Player) && !(en == null) && !(en instanceof Villager) && !(en instanceof NPC)) {
                    if (entityType.equals(en.getType())) {
                        en.remove();
                        counter++;
                    } 
                } else if ((en instanceof Villager) && entityType.equals(EntityType.VILLAGER)) { // only allow if the player has specified its a villager
                    en.remove();
                    counter++;
                }
            }
        }

        if (counter == 0) {
            p.sendMessage(ChatColor.RED + "No Entities were Found.");
            return true;
        }

        p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "Cleared " + ChatColor.RED.toString() + ChatColor.BOLD + counter
                + ChatColor.GREEN.toString() + ChatColor.BOLD + " Entities in a " + ChatColor.RED.toString() + ChatColor.BOLD
                + radius + ChatColor.GREEN.toString() + ChatColor.BOLD + " block radius.");
        return true;
    }
}
