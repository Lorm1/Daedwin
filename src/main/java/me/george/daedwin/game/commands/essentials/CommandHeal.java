package me.george.daedwin.game.commands.essentials;

import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHeal implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        if (!player.isAdmin()) return true;

        if (args.length == 0) { // /heal
            p.setHealth(20);
            p.setSaturation(20);
            p.sendMessage(ChatColor.AQUA + "You have been healed.");
        } else if (args.length == 1) { // /heal <player>
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                p.sendMessage(ChatColor.RED + "That player is offline.");
                return true;
            }

            target.setHealth(20);
            target.setSaturation(20);
            target.sendMessage(ChatColor.AQUA + "You have been healed.");

            p.sendMessage(ChatColor.GREEN + "Healed player " + ChatColor.YELLOW + target.getName());
        } else {
            p.sendMessage(ChatColor.RED + "Invalid Usage.");
            return false;
        }
        return true;
    }
}
