package me.george.daedwin.game.commands.player.moderation;

import me.george.daedwin.game.player.DaedwinPlayer;
import me.george.daedwin.game.profession.farming.enums.FarmingItem;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandProfessions implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        if (!player.isAdmin()) return true;

        if (args.length != 0) {
            p.sendMessage(ChatColor.RED + "Invalid Usage.");
            return false;
        }

        for (FarmingItem item : FarmingItem.values()) {
            p.getInventory().addItem(item.getItemStack());
        }
        return true;
    }
}
