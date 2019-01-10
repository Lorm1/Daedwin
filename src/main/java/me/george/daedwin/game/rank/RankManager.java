package me.george.daedwin.game.rank;

import me.george.daedwin.Daedwin;
import me.george.daedwin.database.DatabaseAPI;
import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class RankManager {

    public RankManager(Daedwin daedwin) {
    }

    public void setRank(UUID uuid, Rank rank) {
        PreparedStatement ps = DatabaseAPI.prepareStatement("UPDATE player_info SET RANK = ? WHERE UUID = ?");
        try {
            ps.setString(1, String.valueOf(rank));
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (Bukkit.getPlayer(uuid) != null) {
            Player target = Bukkit.getPlayer(uuid);
            DaedwinPlayer daedwinPlayer = DaedwinPlayer.getDaedwinPlayers().get(uuid);

            daedwinPlayer.setRank(rank);
        }
    }

    public Rank getRank(UUID uuid) {
        PreparedStatement sts = DatabaseAPI.prepareStatement("SELECT RANK FROM player_info WHERE UUID = ?");
        try {
            sts.setString(1, uuid.toString());
            ResultSet rs = sts.executeQuery();

            return Rank.valueOf(rs.getString("RANK"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
