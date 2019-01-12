package me.george.daedwin.game.commands.moderation.inventory;

import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CommandInvSee implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        if (!player.isAdmin()) return true;

        if (args.length == 0 || args.length > 1) {
            p.sendMessage(ChatColor.RED + "Invalid Usage");
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            p.sendMessage(ChatColor.RED + "That player is offline");
            return true;
        }

        p.sendMessage(ChatColor.GREEN + "Accessing " + ChatColor.YELLOW + target.getName() + ChatColor.GREEN + "'s inventory...");

        Inventory inventory = target.getInventory();
        p.openInventory(inventory);
        return true;
    }
}
