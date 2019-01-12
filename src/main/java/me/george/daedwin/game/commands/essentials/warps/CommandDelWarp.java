package me.george.daedwin.game.commands.essentials.warps;

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
            p.sendMessage(ChatColor.RED + "Please specify a name!");
            return true;
        }
        if (Daedwin.getInstance().getConfig().getConfigurationSection("warps." + args[0]) == null) {
            p.sendMessage(ChatColor.RED + "Warp " + args[0] + " does not exist!");
            return true;
        }

        Daedwin.getInstance().getConfig().set("warps." + args[0], null);
        Daedwin.getInstance().saveConfig();

        p.sendMessage(ChatColor.GREEN + "Removed warp " + ChatColor.YELLOW + args[0]);
        return true;
    }
}

