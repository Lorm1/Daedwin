package me.george.daedwin.database;

import me.george.daedwin.game.Rank;
import me.george.daedwin.game.player.DaedwinPlayer;
import me.george.daedwin.game.player.PlayerCache;
import me.george.daedwin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class DatabaseAPI {

    public static PreparedStatement prepareStatement(String query) {
        PreparedStatement ps = null;

        try {
            ps = Database.connection.prepareStatement(query);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return ps;
    }

    public static boolean playerExists(final UUID uuid, OfflinePlayer player) {
        try {
            PreparedStatement statement = prepareStatement("SELECT * FROM player_info WHERE UUID = ?");
            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            }

            Utils.log.info("Could not find player " + player.getName() + " with UUID: " + uuid + " in the Database, creating the data now...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void loadPlayer(final UUID uuid, OfflinePlayer player) {
        try {
            if (!playerExists(uuid, player)) {
                PreparedStatement ps = prepareStatement("INSERT INTO player_info(UUID, NAME, RANK, GOLD, ECASH, JOIN_DATE, LAST_LOGIN, LAST_LOGOUT, IS_BANNED, BAN_DURATION, BAN_REASON, BANNED_BY) VALUES ('" + uuid.toString() + "'," + "'" + player.getName() + "', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);");
                ps.executeUpdate();

                Utils.log.info("Created Player " + player.getName() + " and added to the Database.");
            }

            PreparedStatement statement = prepareStatement("SELECT * FROM player_info WHERE UUID = ?");
            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();
            rs.next();

            DaedwinPlayer rpgPlayer = new DaedwinPlayer(player);

            String playerName = rs.getString("NAME");
            String rank = rs.getString("RANK");

            int gold = rs.getInt("GOLD");
            int ecash = rs.getInt("ECASH");

            Timestamp joinDate = rs.getTimestamp("JOIN_DATE");
            Timestamp LAST_LOGIN = new Timestamp(System.currentTimeMillis()); // login
            Timestamp LAST_LOGOUT = rs.getTimestamp("LAST_LOGOUT");

            Boolean isPlayerBanned = rs.getBoolean("IS_BANNED");

            Time BAN_DURATION = rs.getTime("BAN_DURATION");

            String BAN_REASON = rs.getString("BAN_REASON");
            String BANNED_BY = rs.getString("BANNED)_BY");

            // Load player data
            rpgPlayer.setRank(Rank.valueOf(rank));

            rpgPlayer.setGold(gold);
            rpgPlayer.setEcash(ecash);

            rpgPlayer.setJoinDate(joinDate);
            rpgPlayer.setLastLogin(LAST_LOGIN);
            rpgPlayer.setLastLogout(LAST_LOGOUT);

            rpgPlayer.setIsBanned(isPlayerBanned);

            if (isPlayerBanned) {
                rpgPlayer.setBanDuration(BAN_DURATION);
                rpgPlayer.setBanReason(BAN_REASON);
                rpgPlayer.setBannedBy(BANNED_BY);
            }

            Utils.log.info("Loaded player " + player.getName());

            PlayerCache.offlineDaedwinPlayerCache.put(player, rpgPlayer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void savePlayer(UUID uuid) {
        try {
            Player player = Bukkit.getPlayer(uuid);
            DaedwinPlayer daedwinPlayer = DaedwinPlayer.getDaedwinPlayers().get(player.getUniqueId());

            PreparedStatement ps = DatabaseAPI.prepareStatement("UPDATE player_info SET NAME = ?, RANK = ?, GOLD = ?, ECASH = ?, JOIN_DATE = ?, LAST_LOGIN = ?, LAST_LOGOUT = ?, IS_BANNED = ?, BAN_DURATION = ?, BAN_REASON = ?, BANNED_BY WHERE UUID ='" + uuid.toString() + "';");

            ps.setString(1,player.getName());
            ps.setString(2, String.valueOf(daedwinPlayer.getRank()));

            ps.setInt(3, daedwinPlayer.getGold());
            ps.setInt(4, daedwinPlayer.getEcash());

            ps.setTimestamp(5, daedwinPlayer.getJoinDate());
            ps.setTimestamp(6, daedwinPlayer.getLastLogin());
            ps.setTimestamp(7, daedwinPlayer.getLastLogout());

            ps.setBoolean(8, daedwinPlayer.getIsBanned());

            ps.setTime(9, daedwinPlayer.getBanDuration());

            ps.setString(10, daedwinPlayer.getBanReason());
            ps.setString(11, daedwinPlayer.getBannedBy());

            ps.executeUpdate();
            System.out.println("Successfully saved " + player.getName() + " 's data.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
