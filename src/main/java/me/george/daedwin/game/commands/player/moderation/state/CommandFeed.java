package me.george.daedwin.game.commands.player.moderation.state;

import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFeed implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        if (!player.isAdmin()) return true;

        if (args.length == 0) { // /feed
            p.setFoodLevel(20);
            p.sendMessage(ChatColor.GREEN + "Your apetite has been saturated");
        } else if (args.length == 1) { // /feed <player>
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                p.sendMessage(ChatColor.RED + "That player is offline.");
                return true;
            }

            target.setFoodLevel(20);
            target.sendMessage(ChatColor.GREEN + "Your apetite has been saturated.");

            p.sendMessage(ChatColor.GREEN + "Fed player " + ChatColor.YELLOW + target.getName());
        } else {
            p.sendMessage(ChatColor.RED + "Invalid Usage.");
            return false;
        }
        return true;
    }
}
