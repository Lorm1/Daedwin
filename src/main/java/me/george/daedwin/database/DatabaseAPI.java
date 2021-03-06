package me.george.daedwin.database;

import me.george.daedwin.Daedwin;
import me.george.daedwin.game.nation.Nation;
import me.george.daedwin.game.player.DaedwinPlayer;
import me.george.daedwin.game.rank.Rank;
import me.george.daedwin.utils.Utils;
import org.bukkit.ChatColor;
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

    public static Nation getPlayerNation(UUID uuid) {
        if (!playerExists(uuid)) throw new NullPointerException("Player with UUID: " + uuid.toString() + " has never played before.");

        PreparedStatement sts;
        try {
            sts = prepareStatement("SELECT NATION FROM player_info WHERE UUID = ?");
            sts.setString(1, uuid.toString());

            ResultSet rs = sts.executeQuery();

            if (rs.next()) {
                return Nation.valueOf(rs.getString("NATION"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("Player with UUID: " + uuid.toString() + " has never played before.");
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

    public static int getPlayerLevel(UUID uuid) {
        if (!playerExists(uuid)) throw new NullPointerException("Player with UUID: " + uuid.toString() + " has never played before.");

        PreparedStatement sts;
        try {
            sts = prepareStatement("SELECT LEVEL FROM player_info WHERE UUID = ?");
            sts.setString(1, uuid.toString());

            ResultSet rs = sts.executeQuery();

            if (rs.next()) {
                return rs.getInt("LEVEL");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("Player with UUID: + " + uuid.toString() + " has never played before.");
    }

    public static int getPlayerGold(UUID uuid) {
        if (!playerExists(uuid)) throw new NullPointerException("Player with UUID: " + uuid.toString() + " has never played before.");

        PreparedStatement sts;
        try {
            sts = prepareStatement("SELECT LEVEL FROM player_info WHERE UUID = ?");
            sts.setString(1, uuid.toString());

            ResultSet rs = sts.executeQuery();

            if (rs.next()) {
                return rs.getInt("GOLD");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("Player with UUID: + " + uuid.toString() + " has never played before.");
    }

    public static int getPlayerEcash(UUID uuid) {
        if (!playerExists(uuid)) throw new NullPointerException("Player with UUID: " + uuid.toString() + " has never played before.");

        PreparedStatement sts;
        try {
            sts = prepareStatement("SELECT LEVEL FROM player_info WHERE UUID = ?");
            sts.setString(1, uuid.toString());

            ResultSet rs = sts.executeQuery();

            if (rs.next()) {
                return rs.getInt("ECASH");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("Player with UUID: + " + uuid.toString() + " has never played before.");
    }

    public static void loadPlayer(Player player) {
        try {
            if (!playerExists(player.getUniqueId())) {
                PreparedStatement ps = prepareStatement("INSERT INTO player_info(UUID, NAME, NATION, RANK, LEVEL, GOLD, ECASH, JOIN_DATE, LAST_LOGIN, IS_BANNED, IS_MUTED) VALUES ('" + player.getUniqueId().toString() + "'," + "'" + player.getName() + "',DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);");
                ps.executeUpdate();
                ps.close();

                Utils.log.info("Created Data for Player " + player.getName() + " and added to the Database.");
            }

            DaedwinPlayer daedwinPlayer = DaedwinPlayer.getInstanceOfPlayer(player);

            PreparedStatement statement = prepareStatement("SELECT * FROM player_info WHERE UUID = ?");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet rs = statement.executeQuery();

            rs.next();

            String rank = rs.getString("RANK");

            int level = rs.getInt("LEVEL");

            String nation = rs.getString("NATION");

            int gold = rs.getInt("GOLD");
            int ecash = rs.getInt("ECASH");

            Timestamp joinDate = rs.getTimestamp("JOIN_DATE");
            Timestamp LAST_LOGIN = new Timestamp(System.currentTimeMillis()); // login

            Boolean isPlayerBanned = rs.getBoolean("IS_BANNED");
            Boolean isPlayerMuted = rs.getBoolean("IS_MUTED");

//            Timestamp BAN_DURATION = rs.getTimestamp("BAN_DURATION");
//
//            String BAN_REASON = rs.getString("BAN_REASON");
//            String BANNED_BY = rs.getString("BANNED)_BY");

            // Load player data
            daedwinPlayer.setNation(Nation.valueOf(nation));
            daedwinPlayer.setRank(Rank.valueOf(rank));

            daedwinPlayer.setLevel(level);
            daedwinPlayer.setGold(gold);
            daedwinPlayer.setEcash(ecash);

            daedwinPlayer.setJoinDate(joinDate);
            daedwinPlayer.setLastLogin(LAST_LOGIN);
            daedwinPlayer.setIsMuted(isPlayerMuted);
            String nick = Daedwin.getInstance().getConfig().getString(daedwinPlayer.getName());
            if (nick != null) {
                nick = ChatColor.translateAlternateColorCodes('&', nick);

                daedwinPlayer.getPlayer().setDisplayName(nick);
                daedwinPlayer.getPlayer().setPlayerListName(nick);
                daedwinPlayer.setNickname(nick);
                daedwinPlayer.setHasNickname(true);
            } else {
                daedwinPlayer.setHasNickname(false);
                daedwinPlayer.getPlayer().setDisplayName(daedwinPlayer.getNation().getColor() + daedwinPlayer.getPlayer().getName());
                daedwinPlayer.getPlayer().setPlayerListName(daedwinPlayer.getNation().getColor() + daedwinPlayer.getPlayer().getName());
            }

            rs.close();

            Utils.log.info("Loaded data for Player: " + daedwinPlayer.getPlayer().getName());

            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void savePlayer(Player player) {
        try {
            DaedwinPlayer daedwinPlayer = DaedwinPlayer.getInstanceOfPlayer(player);

            PreparedStatement statement = prepareStatement("SELECT * FROM player_info WHERE UUID = ?");
            statement.setString(1, daedwinPlayer.getPlayer().getUniqueId().toString());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                PreparedStatement ps = prepareStatement("UPDATE player_info SET NAME = ?, NATION = ?, RANK = ?, LEVEL = ?, GOLD = ?, ECASH = ?, JOIN_DATE = ?, LAST_LOGIN = ?, IS_MUTED = ? WHERE UUID ='" + daedwinPlayer.getPlayer().getUniqueId().toString() + "';");

                ps.setString(1, daedwinPlayer.getPlayer().getName());
                ps.setString(2, String.valueOf(daedwinPlayer.getNation()));
                ps.setString(3, String.valueOf(daedwinPlayer.getRank()));

                ps.setInt(4, daedwinPlayer.getLevel());
                ps.setInt(5, daedwinPlayer.getGold());
                ps.setInt(6, daedwinPlayer.getEcash());

                ps.setTimestamp(7, daedwinPlayer.getJoinDate());
                ps.setTimestamp(8, daedwinPlayer.getLastLogin());

//                ps.setBoolean(9, daedwinPlayer.getIsB);
                ps.setBoolean(9, daedwinPlayer.getIsMuted());

                ps.executeUpdate();
                ps.close();

                Utils.log.info("Successfully saved " + daedwinPlayer.getPlayer().getName() + "'s data.");
                return;
            } else {
                loadPlayer(player);
                savePlayer(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
