package me.george.daedwin.database;

import me.george.daedwin.Daedwin;
import me.george.daedwin.game.nation.Nation;
import me.george.daedwin.game.player.DaedwinPlayer;
import me.george.daedwin.game.rank.Rank;
import me.george.daedwin.utils.Utils;
import org.bukkit.Bukkit;
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

    public static void loadPlayer(DaedwinPlayer daedwinPlayer) { // is online
        try {
            if (!playerExists(daedwinPlayer.getPlayer().getUniqueId())) {
                PreparedStatement ps = prepareStatement("INSERT INTO player_info(UUID, NAME, NATION, RANK, GOLD, ECASH, JOIN_DATE, LAST_LOGIN, IS_BANNED, IS_MUTED) VALUES ('" + daedwinPlayer.getPlayer().getUniqueId().toString() + "'," + "'" + daedwinPlayer.getPlayer().getName() + "', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);");
                ps.executeUpdate();
                ps.close();

                Utils.log.info("Created Data for Player " + daedwinPlayer.getPlayer().getName() + " and added to the Database.");
            }

            PreparedStatement statement = prepareStatement("SELECT * FROM player_info WHERE UUID = ?");
            statement.setString(1, daedwinPlayer.getPlayer().getUniqueId().toString());

            ResultSet rs = statement.executeQuery();

            rs.next();

            String rank = rs.getString("RANK");
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

            daedwinPlayer.setGold(gold);
            daedwinPlayer.setEcash(ecash);

            daedwinPlayer.setJoinDate(joinDate);
            daedwinPlayer.setLastLogin(LAST_LOGIN);
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

    public static void savePlayer(DaedwinPlayer daedwinPlayer) {
        try {

            PreparedStatement statement = prepareStatement("SELECT * FROM player_info WHERE UUID = ?");
            statement.setString(1, daedwinPlayer.getPlayer().getUniqueId().toString());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                PreparedStatement ps = prepareStatement("UPDATE player_info SET NAME = ?, NATION = ?, RANK = ?, GOLD = ?, ECASH = ?, JOIN_DATE = ?, LAST_LOGIN = ?, IS_MUTED = ? WHERE UUID ='" + daedwinPlayer.getPlayer().getUniqueId().toString() + "';");

                ps.setString(1, daedwinPlayer.getPlayer().getName());
                ps.setString(2, String.valueOf(daedwinPlayer.getNation()));
                ps.setString(3, String.valueOf(daedwinPlayer.getRank()));

                ps.setInt(4, daedwinPlayer.getGold());
                ps.setInt(5, daedwinPlayer.getEcash());

                ps.setTimestamp(6, daedwinPlayer.getJoinDate());
                ps.setTimestamp(7, daedwinPlayer.getLastLogin());

//                ps.setBoolean(8, daedwinPlayer.getIsB);
                ps.setBoolean(8, daedwinPlayer.getIsMuted());

                ps.executeUpdate();
                ps.close();

                Utils.log.info("Successfully saved " + daedwinPlayer.getPlayer().getName() + "'s data.");
                return;
            } else {
                loadPlayer(daedwinPlayer);
                savePlayer(daedwinPlayer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void lookup(UUID toGet, Player player) {
        try {
            if (playerExists(toGet)) {
                PreparedStatement statement = prepareStatement("SELECT * FROM player_info WHERE UUID = ?");
                statement.setString(1, toGet.toString());

                ResultSet rs = statement.executeQuery();

                rs.next();

                String playerName = rs.getString("NAME");
                String nation = rs.getString("NATION");
                String rank = rs.getString("RANK");

                Integer gold = rs.getInt("GOLD");
                Integer ecash = rs.getInt("ECASH");

                Timestamp joinDate = rs.getTimestamp("JOIN_DATE");
                Timestamp LAST_LOGIN = new Timestamp(System.currentTimeMillis()); // login

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
