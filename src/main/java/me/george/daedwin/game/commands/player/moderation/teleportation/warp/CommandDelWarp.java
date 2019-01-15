package me.george.daedwin.game.commands.player.moderation.teleportation.warp;

import me.george.daedwin.Daedwin;
import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandDelWarp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) return true;
        Player p = (Player) sender;
        DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        if (!player.isAdmin()) return true;

        if (args.length == 0) {
            p.sendMessage(ChatColor.RED + "You did not specify a Warp.");
            return true;
        } else if (args.length == 1) {

            if (Daedwin.getInstance().getConfig().getConfigurationSection("Warps." + args[0]) == null) {
                p.sendMessage(ChatColor.RED + "Warp " + args[0] + " does not exist.");
                return true;
            }

            Daedwin.getInstance().getConfig().set("Warps." + args[0], null);
            Daedwin.getInstance().saveConfig();

            p.sendMessage(ChatColor.GREEN + "Removed warp " + ChatColor.YELLOW + args[0]);
        } else {
            p.sendMessage(ChatColor.RED + "Invalid Usage.");
            return false;
        }
        return true;
    }
}

