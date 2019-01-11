package me.george.daedwin.game.commands.essentials;

import me.george.daedwin.game.player.DaedwinPlayer;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CommandSpawnEntity implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        DaedwinPlayer daedwinPlayer = DaedwinPlayer.getDaedwinPlayers().get(player.getUniqueId());

        if (!daedwinPlayer.isAdmin()) return true;

        if (args.length == 0 || args.length > 2) {
            player.sendMessage(ChatColor.RED + "Invalid Usage.");
            return false;
        }

        World world = player.getLocation().getWorld();
        Location playerEyeLocation = player.getEyeLocation();
        Vector direction = playerEyeLocation.getDirection().normalize();
        Location front = playerEyeLocation.add(direction);

        if (!EnumUtils.isValidEnum(EntityType.class, args[0].toUpperCase())) {
            player.sendMessage(ChatColor.RED + "Invalid Entity Type.");
            return true;
        }

        EntityType entityType = null;

        int amount = 1;

        if (args.length == 1) { // /spawnmob <mob>
            try {
                entityType = EntityType.valueOf(args[0].toUpperCase());
            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "Invalid Entity.");
                return false;
            }
        }

        if (args.length == 2) { // /spawnmob <mob> <amount>
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid Number.");
                return false;
            }

            try {
                entityType = EntityType.valueOf(args[0].toUpperCase());
            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "Invalid Entity.");
                return false;
            }
        }

        for (int i = 0; i < amount; i++) {
            world.spawnEntity(front, entityType);
        }

        player.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "Spawned Entity: " + ChatColor.RED.toString() + ChatColor.UNDERLINE + entityType.name()
                + ChatColor.GREEN.toString() + ChatColor.BOLD + "  Amount: "
                + ChatColor.RED.toString() + ChatColor.BOLD + amount);
        return true;
    }
}
