package me.george.daedwin.game.commands.moderation.punishment.ban;

import me.george.daedwin.Daedwin;
import me.george.daedwin.database.DatabaseAPI;
import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
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

            if (!player.isStaff()) return true;

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

            p.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------------------------------------");

            p.sendMessage(ChatColor.GRAY + "Name: " + ChatColor.YELLOW + args[0]);
            p.sendMessage(ChatColor.GRAY + "UUID: " + ChatColor.YELLOW + targetUUID.toString());

            sender.sendMessage(ChatColor.GRAY + "Banned: " + (Daedwin.getInstance().getPunishmentManager().getBanManager().isBanned(targetUUID) ? ChatColor.GREEN + "✔" : ChatColor.RED + "✖"));

            Bukkit.getScheduler().runTaskAsynchronously(Daedwin.getInstance(), () -> {
                if(Daedwin.getInstance().getPunishmentManager().getBanManager().isBanned(targetUUID)){
                    p.sendMessage("");
                    p.sendMessage(ChatColor.BLUE.toString() + ChatColor.UNDERLINE + "Reason" + ChatColor.BLUE + ": "+ ChatColor.WHITE + Daedwin.getInstance().getPunishmentManager().getBanManager().getReason(targetUUID));
                    p.sendMessage(ChatColor.GOLD.toString() + ChatColor.UNDERLINE + "Expires" + ChatColor.GOLD + ": " + ChatColor.DARK_RED + Daedwin.getInstance().getPunishmentManager().getBanManager().getTimeLeft(targetUUID));
                }

                p.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------------------------------------");
            });
        }
        return true;
    }
}
