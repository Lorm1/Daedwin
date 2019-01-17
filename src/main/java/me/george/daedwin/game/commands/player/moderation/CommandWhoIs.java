package me.george.daedwin.game.commands.player.moderation;

import me.george.daedwin.Daedwin;
import me.george.daedwin.database.DatabaseAPI;
import me.george.daedwin.game.nation.Nation;
import me.george.daedwin.game.player.DaedwinPlayer;
import me.george.daedwin.game.rank.Rank;
import me.george.daedwin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class CommandWhoIs implements CommandExecutor {

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

            lookup(targetUUID, p);
        }
        return true;
    }

    public void lookup(UUID toGet, Player player) {
        try {
            if (DatabaseAPI.playerExists(toGet)) {
                PreparedStatement statement = DatabaseAPI.prepareStatement("SELECT * FROM player_info WHERE UUID = ?");
                statement.setString(1, toGet.toString());

                ResultSet rs = statement.executeQuery();

                rs.next();

                String playerName = rs.getString("NAME");
                String nation = rs.getString("NATION");
                String rank = rs.getString("RANK");

                Integer gold = rs.getInt("GOLD");
                Integer ecash = rs.getInt("ECASH");

                Timestamp joinDate = rs.getTimestamp("JOIN_DATE");
                Timestamp LAST_LOGIN = new Timestamp(System.currentTimeMillis());

                Boolean isPlayerBanned = rs.getBoolean("IS_BANNED");
                Boolean isPlayerMuted = rs.getBoolean("IS_MUTED");

                player.sendMessage(ChatColor.AQUA + "Retrieving data for player " + ChatColor.YELLOW + playerName + ChatColor.AQUA + "...");

                player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.STRIKETHROUGH + "---------------------------------------------------");

                player.sendMessage("");

                player.sendMessage(ChatColor.GRAY + "Name: " + ChatColor.YELLOW + playerName);
                player.sendMessage(ChatColor.GRAY + "Nation: " + Nation.valueOf(nation).getPrefix());
                player.sendMessage(ChatColor.GRAY + "UUID: " + toGet.toString());
                player.sendMessage(ChatColor.GRAY + "Nickname: " + (Bukkit.getPlayer(toGet).isOnline() ? DaedwinPlayer.getDaedwinPlayers().get(toGet).getNickname() : ChatColor.RED + "Unavailable (Player not Online)"));

                player.sendMessage(ChatColor.GRAY + "Banned: " + (isPlayerBanned ? ChatColor.GREEN + "✔" + ChatColor.BLUE + " Reason: "
                        + ChatColor.WHITE + Daedwin.getInstance().getPunishmentManager().getBanManager().getReason(toGet) + ChatColor.GRAY + " - "+ ChatColor.GOLD + "Expires: "
                        + ChatColor.DARK_RED + Daedwin.getInstance().getPunishmentManager().getBanManager().getTimeLeft(toGet) : ChatColor.RED + "✖"));
                player.sendMessage(ChatColor.GRAY + "Muted: " + (isPlayerMuted ? ChatColor.GREEN + "✔" + ChatColor.BLUE + " Reason: "
                        + ChatColor.WHITE + Daedwin.getInstance().getPunishmentManager().getMuteManager().getReason(toGet) + ChatColor.GRAY + " - " + ChatColor.GOLD + "Expires: "
                        + ChatColor.DARK_RED + Daedwin.getInstance().getPunishmentManager().getMuteManager().getTimeLeft(toGet) : ChatColor.RED + "✖"));

                player.sendMessage(ChatColor.GRAY + "Rank: " + Rank.getColor(Rank.valueOf(rank)) + rank);
                player.sendMessage(ChatColor.GRAY + "Gold: " + ChatColor.GOLD + gold.toString());
                player.sendMessage(ChatColor.GRAY + "E-Cash: " + ChatColor.GREEN + ecash.toString());
                player.sendMessage(ChatColor.GRAY + "Join Date: " + ChatColor.DARK_GREEN + joinDate.toString());
                player.sendMessage(ChatColor.GRAY + "Last Seen: " + ChatColor.DARK_GREEN + LAST_LOGIN.toString());

                player.sendMessage("");

                player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.STRIKETHROUGH + "---------------------------------------------------");

                rs.close();
                return;
            } else {
                Utils.log.info("Could not grab and show this player's data as he has never played before.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
