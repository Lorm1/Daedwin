package me.george.daedwin.database;

import me.george.daedwin.game.Rank;
import me.george.daedwin.game.player.DaedwinPlayer;
import me.george.daedwin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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

    public static boolean playerExists(final UUID uuid) {
        try {
            PreparedStatement statement = prepareStatement("SELECT * FROM player_info WHERE UUID = ?");
            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean playerExists(String name) {
        try {
            PreparedStatement statement = prepareStatement("SELECT * FROM player_info WHERE NAME = ?");
            statement.setString(1, name);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static UUID getPlayerUUID(String playerName) {
        if (!playerExists(playerName)) throw new NullPointerException("Player: " + playerName + " has never played before.");

        PreparedStatement sts;
        try {
            sts = prepareStatement("SELECT UUID FROM player_info WHERE NAME = ?");
            sts.setString(1, playerName);

            ResultSet rs = sts.executeQuery();

            if (rs.next()) {
                return UUID.fromString(rs.getString("UUID"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("Player: " + playerName + " has never played before.");
    }

    public static void loadPlayer(final UUID uuid, OfflinePlayer player) { // is offline -> login
        try {
            if (!playerExists(uuid)) {
                PreparedStatement ps = prepareStatement("INSERT INTO player_info(UUID, NAME, RANK, GOLD, ECASH, JOIN_DATE, LAST_LOGIN, LAST_LOGOUT, IS_BANNED, BAN_DURATION, BAN_REASON, BANNED_BY) VALUES ('" + uuid.toString() + "'," + "'" + player.getName() + "', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);");
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

//            Timestamp BAN_DURATION = rs.getTimestamp("BAN_DURATION");
//
//            String BAN_REASON = rs.getString("BAN_REASON");
//            String BANNED_BY = rs.getString("BANNED)_BY");

            // Load player data
            rpgPlayer.setRank(Rank.valueOf(rank));

            rpgPlayer.setGold(gold);
            rpgPlayer.setEcash(ecash);

            rpgPlayer.setJoinDate(joinDate);
            rpgPlayer.setLastLogin(LAST_LOGIN);
            rpgPlayer.setLastLogout(LAST_LOGOUT);

//            rpgPlayer.setIsBanned(isPlayerBanned);
//
//            if (isPlayerBanned) {
//                rpgPlayer.setBanDuration(Daedwin.getInstance().getBanManager().getTimeLeft(rpgPlayer.getPlayer().getUniqueId()));
//                rpgPlayer.setBanReason(Daedwin.getInstance().getBanManager().getReason(rpgPlayer.getPlayer().getUniqueId()));
//            }

            Utils.log.info("Loaded player " + player.getName());

            rs.close();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadPlayer(final UUID uuid) { // is online
        try {
            if (!playerExists(uuid)) {
                PreparedStatement ps = prepareStatement("INSERT INTO player_info(UUID, NAME, RANK, GOLD, ECASH, JOIN_DATE, LAST_LOGIN, LAST_LOGOUT, IS_BANNED, BAN_DURATION, BAN_REASON, BANNED_BY) VALUES ('" + uuid.toString() + "'," + "'" + Bukkit.getPlayer(uuid).getName() + "', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);");
                ps.executeUpdate();

                Utils.log.info("Created Player " + Bukkit.getPlayer(uuid).getName() + " and added to the Database.");
            }

            PreparedStatement statement = prepareStatement("SELECT * FROM player_info WHERE UUID = ?");
            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();
            rs.next();

            DaedwinPlayer rpgPlayer;

            if (!DaedwinPlayer.getDaedwinPlayers().containsKey(uuid)) {
                rpgPlayer = new DaedwinPlayer(uuid);
                DaedwinPlayer.getDaedwinPlayers().put(uuid, rpgPlayer);
            } else { // for whatever reason
                rpgPlayer = DaedwinPlayer.getDaedwinPlayers().get(uuid);
            }

            String playerName = rs.getString("NAME");
            String rank = rs.getString("RANK");

            int gold = rs.getInt("GOLD");
            int ecash = rs.getInt("ECASH");

            Timestamp joinDate = rs.getTimestamp("JOIN_DATE");
            Timestamp LAST_LOGIN = new Timestamp(System.currentTimeMillis()); // login
            Timestamp LAST_LOGOUT = rs.getTimestamp("LAST_LOGOUT");

            Boolean isPlayerBanned = rs.getBoolean("IS_BANNED");

//            Timestamp BAN_DURATION = rs.getTimestamp("BAN_DURATION");
//
//            String BAN_REASON = rs.getString("BAN_REASON");
//            String BANNED_BY = rs.getString("BANNED)_BY");

            // Load player data
            rpgPlayer.setRank(Rank.valueOf(rank));

            rpgPlayer.setGold(gold);
            rpgPlayer.setEcash(ecash);

            rpgPlayer.setJoinDate(joinDate);
            rpgPlayer.setLastLogin(LAST_LOGIN);
            rpgPlayer.setLastLogout(LAST_LOGOUT);

//            rpgPlayer.setIsBanned(isPlayerBanned);

//            if (isPlayerBanned) {
//                rpgPlayer.setBanDuration(Daedwin.getInstance().getBanManager().getTimeLeft(rpgPlayer.getPlayer().getUniqueId()));
//                rpgPlayer.setBanReason(Daedwin.getInstance().getBanManager().getReason(rpgPlayer.getPlayer().getUniqueId()));
//            }

            Utils.log.info("Loading " + Bukkit.getPlayer(uuid).getName() + "'s data...");

            rs.close();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void savePlayer(UUID uuid) {
        try {
            Player player = Bukkit.getPlayer(uuid);
            DaedwinPlayer daedwinPlayer = DaedwinPlayer.getDaedwinPlayers().get(player.getUniqueId());

            PreparedStatement statement = prepareStatement("SELECT * FROM player_info WHERE UUID = ?");

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                PreparedStatement ps = prepareStatement("UPDATE player_info SET NAME = ?, RANK = ?, GOLD = ?, ECASH = ?, JOIN_DATE = ?, LAST_LOGIN = ?, LAST_LOGOUT = ?, IS_BANNED = ? WHERE UUID ='" + uuid.toString() + "';");

                ps.setString(1, player.getName());
                ps.setString(2, String.valueOf(daedwinPlayer.getRank()));

                ps.setInt(3, daedwinPlayer.getGold());
                ps.setInt(4, daedwinPlayer.getEcash());

                ps.setTimestamp(5, daedwinPlayer.getJoinDate());
                ps.setTimestamp(6, daedwinPlayer.getLastLogin());
                ps.setTimestamp(7, daedwinPlayer.getLastLogout());

//                ps.setBoolean(8, daedwinPlayer.getIsBanned());

                ps.executeUpdate();
                ps.close();
                System.out.println("Successfully saved " + player.getName() + " 's data.");
                return;
            } else {
                loadPlayer(uuid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
