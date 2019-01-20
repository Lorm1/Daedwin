package me.george.daedwin.game.punishment.mute;

import me.george.daedwin.database.DatabaseAPI;
import me.george.daedwin.game.player.DaedwinPlayer;
import me.george.daedwin.game.punishment.PunishmentManager;
import me.george.daedwin.utils.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MuteManager {

    public MuteManager(PunishmentManager punishmentManager) {
    }

    public void mute(UUID uuid, long endInSeconds, String reason) {
        if (isMuted(uuid)) return;

        long endToMillis = endInSeconds * 1000;
        long end = endToMillis + System.currentTimeMillis();

        if (endInSeconds == -1) {
            end = -1;
        }

        PreparedStatement sts = DatabaseAPI.prepareStatement("INSERT INTO muted_players (UUID, NAME, MUTE_REASON, MUTE_DURATION) VALUES (?, ?, ?, ?)");
        try {
            sts.setString(1, uuid.toString());
            sts.setString(2, Bukkit.getPlayer(uuid) != null ? Bukkit.getPlayer(uuid).getName() : Bukkit.getOfflinePlayer(uuid).getName());
            sts.setString(3, reason);
            sts.setLong(4, end);
            sts.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PreparedStatement ps = DatabaseAPI.prepareStatement("UPDATE player_info SET IS_MUTED = ? WHERE UUID = ?");
        try {
            ps.setBoolean(1, true);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (Bukkit.getPlayer(uuid) != null) {
            Player target = Bukkit.getPlayer(uuid);
            String muteMessage = ChatColor.RED.toString() + ChatColor.UNDERLINE + "You have been muted for" + ChatColor.RED + ": "
                    + ChatColor.WHITE + reason
                    + " " + ChatColor.RED.toString() + ChatColor.UNDERLINE + "Expires" + ChatColor.RED + ": " + ChatColor.DARK_RED + getTimeLeft(uuid);

            target.sendMessage(muteMessage);
            target.sendTitle(ChatColor.RED + "You have been muted.", null, 20 * 1, 20 * 2, 20 * 2);

            DaedwinPlayer.getDaedwinPlayers().get(target.getUniqueId()).setIsMuted(true);
        }
    }

    public void unmute(UUID uuid) {
        if (!isMuted(uuid)) return;

        PreparedStatement sts = DatabaseAPI.prepareStatement("DELETE FROM muted_players WHERE UUID = ?");
        try {
            sts.setString(1, uuid.toString());
            sts.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PreparedStatement ps = DatabaseAPI.prepareStatement("UPDATE player_info SET IS_MUTED = ? WHERE UUID = ?");
        try {
            ps.setBoolean(1, false);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (Bukkit.getPlayer(uuid) != null) {
            Player target = Bukkit.getPlayer(uuid);

            target.sendMessage(ChatColor.RED + "You are no longer muted.");

            DaedwinPlayer.getDaedwinPlayers().get(target.getUniqueId()).setIsMuted(false);
        }
    }

    public boolean isMuted(UUID uuid) {
        try {
            PreparedStatement sts = DatabaseAPI.prepareStatement("SELECT * FROM muted_players WHERE UUID = ?");
            sts.setString(1, uuid.toString());

            ResultSet rs = sts.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public long getEnd(UUID uuid) {
        if (!isMuted(uuid)) return 0;

        PreparedStatement sts = DatabaseAPI.prepareStatement("SELECT * FROM muted_players WHERE UUID = ?");
        try {
            sts.setString(1, uuid.toString());
            ResultSet rs = sts.executeQuery();

            if (rs.next()) {
                return rs.getLong("MUTE_DURATION");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void checkDuration(UUID uuid) {
        if (!isMuted(uuid)) return;

        if (getEnd(uuid) == -1) return;

        if (getEnd(uuid) < System.currentTimeMillis()) {
            unmute(uuid);
        }
    }

    public String getTimeLeft(UUID uuid) {
        if (!isMuted(uuid)) return "Not muted.";

        if (getEnd(uuid) == -1) return "Never";

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
        if (!isMuted(uuid)) return "Not muted.";

        PreparedStatement sts = DatabaseAPI.prepareStatement("SELECT * FROM MUTED_PLAYERS WHERE UUID=?");
        try {
            sts.setString(1, uuid.toString());
            ResultSet rs = sts.executeQuery();

            if (rs.next()) {
                return rs.getString("MUTE_REASON");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Not muted.";
    }
}
