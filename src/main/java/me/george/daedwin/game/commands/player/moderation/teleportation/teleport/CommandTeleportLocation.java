package me.george.daedwin.game.commands.player.moderation.teleportation.teleport;

import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTeleportLocation implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        if (!player.isAdmin()) return true;

        if (args.length != 3) {
            p.sendMessage(ChatColor.RED + "Invalid Coordinates.");
            return false;
        }

        Location location;

        try {
            location = new Location(p.getWorld(), Double.valueOf(args[0]), Double.valueOf(args[1]), Double.valueOf(args[2]));
            p.teleport(location);
            p.sendMessage(ChatColor.GREEN + "Teleported you to " + ChatColor.YELLOW + location.getX() + ", " + location.getY() + ", " + location.getZ());
        } catch (Exception e) {
            p.sendMessage(ChatColor.RED + "Invalid Coordinates.");
            return false;
        }
        return true;
    }
}
