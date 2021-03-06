package me.george.daedwin.game.commands.player.moderation.punishment.mute;

import me.george.daedwin.Daedwin;
import me.george.daedwin.database.DatabaseAPI;
import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandUnmute implements CommandExecutor {

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

            Bukkit.getScheduler().runTaskAsynchronously(Daedwin.getInstance(), () -> Daedwin.getInstance().getPunishmentManager().getMuteManager().unmute(targetUUID));
            p.sendMessage(ChatColor.RED + "Unmuted player " + targetName);

        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length != 1) {
                sender.sendMessage(ChatColor.RED + "Invalid Usage.");
                return false;
            }

            String targetName = args[0];

            if (!DatabaseAPI.playerExists(targetName)) {
                sender.sendMessage(ChatColor.RED + "That player has never played before.");
                return true;
            }

            UUID targetUUID = DatabaseAPI.getPlayerUUID(targetName);

            Bukkit.getScheduler().runTaskAsynchronously(Daedwin.getInstance(), () -> Daedwin.getInstance().getPunishmentManager().getMuteManager().unmute(targetUUID));
            sender.sendMessage(ChatColor.RED + "Unmuted player " + targetName);
        }
        return true;
    }
}
