package me.george.daedwin.game.commands.essentials.warps;

import me.george.daedwin.Daedwin;
import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;

public class CommandWarp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player p = (Player) sender;
        DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        if (!player.isAdmin()) return true;

        if (args.length == 0) { // /warp
            p.sendMessage(ChatColor.RED + "You did not specify a Warp.");
            return false;
        } else if (args.length == 1) { // /warp <name>
            if (args[0].equalsIgnoreCase("list")) {
                ArrayList<String> list = new ArrayList<>();
                ConfigurationSection section = Daedwin.getInstance().getConfig().getConfigurationSection("Warps");
                Set<String> warps = section.getKeys(false);
                for (String warpName : warps) {
                    list.add(warpName);
                }

                p.sendMessage(ChatColor.BLUE.toString() + ChatColor.BOLD + "Warps:\n");
                p.sendMessage(ChatColor.GRAY + list.iterator().next());
                return true;
            }

            if (Daedwin.getInstance().getConfig().getConfigurationSection("Warps." + args[0]) == null) {
                p.sendMessage(ChatColor.RED + "Warp " + args[0] + " does not exist!");
                return true;
            }

            World w = Bukkit.getServer().getWorld(Daedwin.getInstance().getConfig().getString("Warps." + args[0] + ".world"));

            double x = Daedwin.getInstance().getConfig().getDouble("Warps." + args[0] + ".x");
            double y = Daedwin.getInstance().getConfig().getDouble("Warps." + args[0] + ".y");
            double z = Daedwin.getInstance().getConfig().getDouble("Warps." + args[0] + ".z");

            p.teleport(new Location(w, x, y, z));
            p.sendMessage(ChatColor.GREEN + "Warped to " + ChatColor.YELLOW + args[0]);
        } else {
            p.sendMessage(ChatColor.RED + "Invalid Usage.");
            return false;
        }
        return true;
    }
}
