package me.george.daedwin.game.commands;

import me.george.daedwin.Daedwin;
import me.george.daedwin.database.DatabaseAPI;
import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandCheckBan implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

            if (!player.isAdmin()) return true;

            if (args.length != 1) {
                p.sendMessage(ChatColor.RED + "Invalid Usage.");
                return false;
            }

            String targetName = args[0];

            if (!DatabaseAPI.playerExists(targetName)) {
                p.sendMessage(ChatColor.RED + "That player has never played before.");
                return true;
            }

            UUID targetUUID = DatabaseAPI.getPlayerUUID(targetName);

            p.sendMessage(ChatColor.GRAY + "&m-----------------------------------------------------");

            p.sendMessage(ChatColor.AQUA + "Name: " + ChatColor.YELLOW + args[0]);
            p.sendMessage("§eUUID : §b" + targetUUID.toString());
            sender.sendMessage("§eBanni : " + (Daedwin.getInstance().getBanManager().isBanned(targetUUID) ? ChatColor.GREEN + "✔" : ChatColor.RED + "✖"));

            if(Daedwin.getInstance().getBanManager().isBanned(targetUUID)){
                p.sendMessage("");
                p.sendMessage(ChatColor.AQUA.toString() + ChatColor.UNDERLINE + "Reason: " + ChatColor.WHITE + Daedwin.getInstance().getBanManager().getReason(targetUUID));
                p.sendMessage(ChatColor.GOLD + "Duration: " + ChatColor.RED + Daedwin.getInstance().getBanManager().getTimeLeft(targetUUID));
            }

            sender.sendMessage(ChatColor.GRAY + "&m-----------------------------------------------------");
        }
        return true;
    }
}
