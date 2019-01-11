package me.george.daedwin.database;

import me.george.daedwin.Daedwin;
import me.george.daedwin.game.player.DaedwinPlayer;
import me.george.daedwin.game.rank.Rank;
import me.george.daedwin.utils.Utils;
import org.bukkit.ChatColor;

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

    public static Rank getPlayerRank(String playerName) {
        if (!playerExists(playerName)) throw new NullPointerException("Player: " + playerName + " has never played before.");

        PreparedStatement sts;
        try {
            sts = prepareStatement("SELECT RANK FROM player_info WHERE NAME = ?");
            sts.setString(1, playerName);

            ResultSet rs = sts.executeQuery();

            if (rs.next()) {
                return Rank.valueOf(rs.getString("RANK"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("Player: " + playerName + " has never played before.");
    }

    public static Rank getPlayerRank(UUID uuid) {
        if (!playerExists(uuid)) throw new NullPointerException("Player with UUID: " + uuid.toString() + " has never played before.");

        PreparedStatement sts;
        try {
            sts = prepareStatement("SELECT RANK FROM player_info WHERE UUID = ?");
            sts.setString(1, uuid.toString());

            ResultSet rs = sts.executeQuery();

            if (rs.next()) {
                return Rank.valueOf(rs.getString("RANK"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("Player with UUID: " + uuid.toString() + " has never played before.");
    }

    public static void loadPlayer(DaedwinPlayer daedwinPlayer) { // is online
        try {
            if (!playerExists(daedwinPlayer.getPlayer().getUniqueId())) {
                PreparedStatement ps = prepareStatement("INSERT INTO player_info(UUID, NAME, RANK, GOLD, ECASH, JOIN_DATE, LAST_LOGIN, LAST_LOGOUT, IS_BANNED, IS_MUTED) VALUES ('" + daedwinPlayer.getPlayer().getUniqueId().toString() + "'," + "'" + daedwinPlayer.getPlayer().getName() + "', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);");
                ps.executeUpdate();
                ps.close();

                Utils.log.info("Created Data for Player " + daedwinPlayer.getPlayer().getName() + " and added to the Database.");
            }

            PreparedStatement statement = prepareStatement("SELECT * FROM player_info WHERE UUID = ?");
            statement.setString(1, daedwinPlayer.getPlayer().getUniqueId().toString());

            ResultSet rs = statement.executeQuery();

            rs.next();

            String playerName = rs.getString("NAME");
            String rank = rs.getString("RANK");

            int gold = rs.getInt("GOLD");
            int ecash = rs.getInt("ECASH");

            Timestamp joinDate = rs.getTimestamp("JOIN_DATE");
            Timestamp LAST_LOGIN = new Timestamp(System.currentTimeMillis()); // login
            Timestamp LAST_LOGOUT = rs.getTimestamp("LAST_LOGOUT");

            Boolean isPlayerBanned = rs.getBoolean("IS_BANNED");
            Boolean isPlayerMuted = rs.getBoolean("IS_MUTED");

//            Timestamp BAN_DURATION = rs.getTimestamp("BAN_DURATION");
//
//            String BAN_REASON = rs.getString("BAN_REASON");
//            String BANNED_BY = rs.getString("BANNED)_BY");

            // Load player data
            daedwinPlayer.setRank(Rank.valueOf(rank));

            daedwinPlayer.setGold(gold);
            daedwinPlayer.setEcash(ecash);

            daedwinPlayer.setJoinDate(joinDate);
            daedwinPlayer.setLastLogin(LAST_LOGIN);
            daedwinPlayer.setLastLogout(LAST_LOGOUT);
            daedwinPlayer.setIsMuted(isPlayerMuted);

//            rpgPlayer.setIsBanned(isPlayerBanned); // we can have it, but not necessary atm.

//            if (isPlayerBanned) {
//                rpgPlayer.setBanDuration(Daedwin.getInstance().getBanManager().getTimeLeft(rpgPlayer.getPlayer().getUniqueId()));
//                rpgPlayer.setBanReason(Daedwin.getInstance().getBanManager().getReason(rpgPlayer.getPlayer().getUniqueId()));
//            }

            String nick = Daedwin.getInstance().getConfig().getString(daedwinPlayer.getName());
            if (nick != null) {
                nick = ChatColor.translateAlternateColorCodes('&', nick);

                daedwinPlayer.getPlayer().setDisplayName(nick);
                daedwinPlayer.getPlayer().setPlayerListName(nick);
                daedwinPlayer.setNickname(nick);
            }

            rs.close();

            Utils.log.info("Loaded data for Player " + daedwinPlayer.getPlayer().getName());

            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void savePlayer(DaedwinPlayer daedwinPlayer) {
        try {

            PreparedStatement statement = prepareStatement("SELECT * FROM player_info WHERE UUID = ?");
            statement.setString(1, daedwinPlayer.getPlayer().getUniqueId().toString());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                PreparedStatement ps = prepareStatement("UPDATE player_info SET NAME = ?, RANK = ?, GOLD = ?, ECASH = ?, JOIN_DATE = ?, LAST_LOGIN = ?, LAST_LOGOUT = ?, IS_MUTED = ? WHERE UUID ='" + daedwinPlayer.getPlayer().getUniqueId().toString() + "';");

                ps.setString(1, daedwinPlayer.getPlayer().getName());
                ps.setString(2, String.valueOf(daedwinPlayer.getRank()));

                ps.setInt(3, daedwinPlayer.getGold());
                ps.setInt(4, daedwinPlayer.getEcash());

                ps.setTimestamp(5, daedwinPlayer.getJoinDate());
                ps.setTimestamp(6, daedwinPlayer.getLastLogin());
                ps.setTimestamp(7, daedwinPlayer.getLastLogout());

//                ps.setBoolean(8, daedwinPlayer.getIsB);
                ps.setBoolean(8, daedwinPlayer.getIsMuted());

                ps.executeUpdate();
                ps.close();

                System.out.println("Successfully saved " + daedwinPlayer.getPlayer().getName() + "'s data.");
                return;
            } else {
                loadPlayer(daedwinPlayer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
