package me.george.daedwin.game.commands.essentials.warps;

import me.george.daedwin.Daedwin;
import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandSetWarp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) return true;
        Player p = (Player) sender;
        DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        if (!player.isAdmin()) return true;

        if (args.length == 0) {
            p.sendMessage(ChatColor.RED + "You did not specify a Warp.");
            return false;
        } else if (args.length == 1) {
            Daedwin.getInstance().getConfig().set("Warps." + args[0] + ".world", p.getLocation().getWorld().getName());
            Daedwin.getInstance().getConfig().set("Warps." + args[0] + ".x", p.getLocation().getX());
            Daedwin.getInstance().getConfig().set("Warps." + args[0] + ".y", p.getLocation().getY());
            Daedwin.getInstance().getConfig().set("Warps." + args[0] + ".z", p.getLocation().getZ());
            Daedwin.getInstance().saveConfig();

            p.sendMessage(ChatColor.GREEN + "Created Warp " + ChatColor.YELLOW + args[0]);
        } else {
            p.sendMessage(ChatColor.RED + "Invalid Usage.");
            return false;
        }
        return true;
    }
}
