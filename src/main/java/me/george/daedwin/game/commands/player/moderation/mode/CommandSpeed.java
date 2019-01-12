package me.george.daedwin.game.commands.player.moderation.mode;

import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpeed implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        if (!player.isAdmin()) return true;

        if (args.length <= 1) {
            p.sendMessage(ChatColor.RED + "Invalid Usage.");
            return false;
        } else if (args.length == 2) {
            float speed;

            try {
                speed = Float.valueOf(args[1]);
            } catch (NumberFormatException e) {
                p.sendMessage(ChatColor.RED + "Invalid Number.");
                return false;
            }

            if (speed < 0.0F) {
                p.sendMessage(ChatColor.RED + "The speed value has to be between 0-10");
                return true;
            }
            if (speed > 10.0F){
                p.sendMessage(ChatColor.RED + "The speed value has to be between 0-10");
                return true;
            }

            speed = speed / 10.0F;

            if (args[0].equalsIgnoreCase("fly")) {
                p.setFlySpeed(speed);
                p.setAllowFlight(true);
                p.sendMessage(ChatColor.AQUA + "Set flying speed to " + ChatColor.YELLOW + args[1]);
            } else if (args[0].equalsIgnoreCase("walk")) {
                p.setWalkSpeed(speed); // default: 0.2f
                p.sendMessage(ChatColor.AQUA + "Set walking speed to " + ChatColor.YELLOW + args[1]);
            }
        } else {
            p.sendMessage(ChatColor.RED + "Invalid Usage.");
            return false;
        }
        return true;
    }
}
