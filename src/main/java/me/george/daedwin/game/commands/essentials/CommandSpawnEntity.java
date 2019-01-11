package me.george.daedwin.game.commands.essentials;

import org.apache.commons.lang3.EnumUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CommandSpawnEntity implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) return true;

        Player player = (Player) sender;
        if (args.length < 1 || args.length > 2) { // /spawnmob
            player.sendMessage(ChatColor.RED + "Invalid Usage.");
            return false;
        }

        World world = player.getLocation().getWorld();
        Location playerEyeLocation = player.getEyeLocation();
        Vector direction = playerEyeLocation.getDirection();
        Location front = playerEyeLocation.add(direction);

        if (!EnumUtils.isValidEnum(EntityType.class, args[0].toUpperCase())) {
            player.sendMessage(ChatColor.RED + "Invalid Entity Type.");
            return true;
        }

        EntityType entityType = EntityType.valueOf(args[0].toUpperCase()); // /spawnmob <mob>
        int amount = 1;

        if (entityType.equals(EntityType.PLAYER)) return true;

        if (args.length == 2) { // /spawnmob <mob> <amount>
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid Number.");
                return false;
            }
        }

        for (int i = 0; i < amount; i++) {
            Entity entity = (Entity) world.spawnEntity(front, entityType);
        }

        player.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "Spawned Entity: " + ChatColor.RED.toString() + ChatColor.UNDERLINE + entityType.name()
                + ChatColor.GREEN.toString() + ChatColor.BOLD + "  Amount: "
                + ChatColor.RED.toString() + ChatColor.BOLD + amount);
        return true;
    }
}
