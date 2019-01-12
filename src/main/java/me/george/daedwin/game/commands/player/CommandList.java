package me.george.daedwin.game.commands.player;

import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandList implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) return true;
        if (!(sender instanceof Player)) return true;

        Player p = (Player) sender;
        DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        if (!player.isStaff()) return true;

        List<String> names = new ArrayList<>();
        int playersOnline = Bukkit.getOnlinePlayers().size();

        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl != null) {
                names.add(pl.getName());
            }
        }

        p.sendMessage(ChatColor.BLUE.toString() + ChatColor.BOLD + "Online Players: " + ChatColor.GOLD + playersOnline);
        p.sendMessage(ChatColor.GRAY + names.toString());
        return true;
    }
}
