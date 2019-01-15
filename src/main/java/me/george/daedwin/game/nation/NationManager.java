package me.george.daedwin.game.nation;

import me.george.daedwin.Daedwin;
import me.george.daedwin.database.DatabaseAPI;
import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class NationManager {

    public NationManager(Daedwin daedwin) {
    }

    public void setNation(UUID uuid, Nation nation) {
        PreparedStatement ps = DatabaseAPI.prepareStatement("UPDATE player_info SET NATION = ? WHERE UUID = ?");
        try {
            ps.setString(1, String.valueOf(nation));
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (Bukkit.getPlayer(uuid) != null) {
            Player target = Bukkit.getPlayer(uuid);
            DaedwinPlayer daedwinPlayer = DaedwinPlayer.getDaedwinPlayers().get(uuid);

            daedwinPlayer.setNation(nation);
        }
    }

    public Nation getNation(UUID uuid) {
        PreparedStatement sts = DatabaseAPI.prepareStatement("SELECT NATION FROM player_info WHERE UUID = ?");
        try {
            sts.setString(1, uuid.toString());
            ResultSet rs = sts.executeQuery();

            return Nation.valueOf(rs.getString("NATION"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
