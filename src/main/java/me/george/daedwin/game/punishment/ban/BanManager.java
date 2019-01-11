package me.george.daedwin.game.punishment.ban;

import me.george.daedwin.database.DatabaseAPI;
import me.george.daedwin.game.punishment.PunishmentManager;
import me.george.daedwin.game.punishment.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BanManager {

    public BanManager(PunishmentManager punishmentManager) {
    }

    public void ban(UUID uuid, long endInSeconds, String reason) {
        if (isBanned(uuid)) return;

        long endToMillis = endInSeconds * 1000;
        long end = endToMillis + System.currentTimeMillis();

        if (endInSeconds == -1) {
            end = -1;
        }

        PreparedStatement sts = DatabaseAPI.prepareStatement("INSERT INTO banned_players (UUID, NAME, BAN_REASON, BAN_DURATION) VALUES (?, ?, ?, ?)");
        try {
            sts.setString(1, uuid.toString());
            sts.setString(2, Bukkit.getPlayer(uuid) != null ? Bukkit.getPlayer(uuid).getName() : Bukkit.getOfflinePlayer(uuid).getName());
            sts.setString(3, reason);
            sts.setLong(4, end);
            sts.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PreparedStatement ps = DatabaseAPI.prepareStatement("UPDATE player_info SET IS_BANNED = ? WHERE UUID = ?");
        try {
            ps.setBoolean(1, true);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (Bukkit.getPlayer(uuid) != null) {
            Player target = Bukkit.getPlayer(uuid);
            String banMessage = ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "Myths of Daedwin\n\n" + ChatColor.RED.toString() + ChatColor.UNDERLINE + "You have been Banned.\n\n"
                    + ChatColor.BLUE.toString() + ChatColor.UNDERLINE + "Reason" + ChatColor.AQUA + ": "
                    + ChatColor.DARK_RED + reason + "\n\n"
                    + ChatColor.GRAY.toString() + ChatColor.UNDERLINE + "Expires: " + ChatColor.RED + getTimeLeft(uuid);

            target.kickPlayer(banMessage);
        }
    }

    public void unban(UUID uuid) {
        if (!isBanned(uuid)) return;

        PreparedStatement sts = DatabaseAPI.prepareStatement("DELETE FROM banned_players WHERE UUID = ?");
        try {
            sts.setString(1, uuid.toString());
            sts.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PreparedStatement ps = DatabaseAPI.prepareStatement("UPDATE player_info SET IS_BANNED = ? WHERE UUID = ?");
        try {
            ps.setBoolean(1, false);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isBanned(UUID uuid) {
        try {
            PreparedStatement sts = DatabaseAPI.prepareStatement("SELECT * FROM banned_players WHERE UUID = ?");
            sts.setString(1, uuid.toString());

            ResultSet rs = sts.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public long getEnd(UUID uuid) {
        if (!isBanned(uuid)) return 0;

        PreparedStatement sts = DatabaseAPI.prepareStatement("SELECT * FROM banned_players WHERE UUID = ?");
        try {
            sts.setString(1, uuid.toString());
            ResultSet rs = sts.executeQuery();

            if (rs.next()) {
                return rs.getLong("BAN_DURATION");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void checkDuration(UUID uuid) {
        if (!isBanned(uuid)) return;

        if (getEnd(uuid) == -1) return;

        if (getEnd(uuid) < System.currentTimeMillis()) {
            unban(uuid);
        }
    }

    public String getTimeLeft(UUID uuid) {
        if (!isBanned(uuid)) return "Not banned.";

        if (getEnd(uuid) == -1) return "Permanent";

        long time = (getEnd(uuid) - System.currentTimeMillis()) / 1000;
        int seconds = 0;
        int minutes = 0;
        int hours = 0;
        int days = 0;
        int weeks = 0;
        int months = 0;

        while (time >= TimeUnit.MONTHS.getToSecond()) {
            months++;
            time -= TimeUnit.MONTHS.getToSecond();
        }

        while (time >= TimeUnit.WEEKS.getToSecond()) {
            weeks++;
            time -= TimeUnit.WEEKS.getToSecond();
        }

        while (time >= TimeUnit.DAYS.getToSecond()) {
            days++;
            time -= TimeUnit.DAYS.getToSecond();
        }

        while (time >= TimeUnit.HOURS.getToSecond()) {
            hours++;
            time -= TimeUnit.HOURS.getToSecond();
        }

        while (time >= TimeUnit.MINUTES.getToSecond()) {
            minutes++;
            time -= TimeUnit.MINUTES.getToSecond();
        }

        while (time >= TimeUnit.SECONDS.getToSecond()) {
            seconds++;
            time -= TimeUnit.SECONDS.getToSecond();
        }

        return months + " " + TimeUnit.MONTHS.getName() + ", "
                + weeks + " " + TimeUnit.WEEKS.getName() + ", "
                + days + " " + TimeUnit.DAYS.getName() + ", "
                + hours + " " + TimeUnit.HOURS.getName() + ", "
                + minutes + " " + TimeUnit.MINUTES.getName() + ", "
                + seconds + " " + TimeUnit.SECONDS.getName();
    }

    public String getReason(UUID uuid) {
        if (!isBanned(uuid)) return "Not banned.";

        PreparedStatement sts = DatabaseAPI.prepareStatement("SELECT * FROM banned_players WHERE UUID=?");
        try {
            sts.setString(1, uuid.toString());
            ResultSet rs = sts.executeQuery();

            if (rs.next()) {
                return rs.getString("BAN_REASON");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Not banned.";
    }
}
