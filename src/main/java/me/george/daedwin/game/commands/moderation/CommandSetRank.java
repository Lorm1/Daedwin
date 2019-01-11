package me.george.daedwin.game.commands.moderation;

import me.george.daedwin.Constants;
import me.george.daedwin.Daedwin;
import me.george.daedwin.database.DatabaseAPI;
import me.george.daedwin.game.player.DaedwinPlayer;
import me.george.daedwin.game.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandSetRank implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

            if (!player.isAdmin()) return true;

            if (args.length < 2) {
                p.sendMessage(ChatColor.RED + "Invalid Usage.");
                return false;
            }

            String targetName = args[0];

            if (!DatabaseAPI.playerExists(targetName)) {
                p.sendMessage(ChatColor.RED + "That player has never played before.");
                return true;
            }

            UUID targetUUID = DatabaseAPI.getPlayerUUID(targetName);

            if (Constants.ADMINS.contains(targetName) || DatabaseAPI.getPlayerRank(targetUUID).equals(Rank.ADMIN) && !(sender instanceof ConsoleCommandSender)) {
                p.sendMessage(ChatColor.RED + "You cannot set this player's rank.");
                return true;
            }

            if (Rank.isValidRank(args[1].toUpperCase())) {
                Rank rank = Rank.valueOf(args[1].toUpperCase());

                Daedwin.getInstance().getRankManager().setRank(targetUUID, rank);
                p.sendMessage(ChatColor.GREEN + "Set " + ChatColor.YELLOW + targetName + ChatColor.GREEN + "'s rank to " + Rank.getColor(rank) + rank.name());
            } else {
                p.sendMessage(ChatColor.RED + "Invalid rank.");
                for (Rank rank : Rank.values()) {
                    p.sendMessage("Valid Ranks: " + Rank.getColor(rank) + rank.name());
                }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Invalid Usage.");
                return false;
            }

            String targetName = args[0];

            if (!DatabaseAPI.playerExists(targetName)) {
                sender.sendMessage(ChatColor.RED + "That player has never played before.");
                return true;
            }

            UUID targetUUID = DatabaseAPI.getPlayerUUID(targetName);

            if (Constants.ADMINS.contains(targetName) || DatabaseAPI.getPlayerRank(targetUUID).equals(Rank.ADMIN) && !(sender instanceof ConsoleCommandSender)) {
                sender.sendMessage(ChatColor.RED + "You cannot set this player's rank.");
                return true;
            }

            if (Rank.isValidRank(args[1].toUpperCase())) {
                Rank rank = Rank.valueOf(args[1].toUpperCase());

                Daedwin.getInstance().getRankManager().setRank(targetUUID, rank);
                sender.sendMessage(ChatColor.GREEN + "Set " + ChatColor.YELLOW + targetName + ChatColor.GREEN + "'s rank to " + Rank.getColor(rank) + rank.name());
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid rank.");
                for (Rank rank : Rank.values()) {
                    sender.sendMessage("Valid Ranks: " + Rank.getColor(rank) + rank.name());
                }
            }
        }
        return true;
    }
}
