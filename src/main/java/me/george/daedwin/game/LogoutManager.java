package me.george.daedwin.game;

import me.george.daedwin.Daedwin;
import me.george.daedwin.database.DatabaseAPI;
import me.george.daedwin.game.player.DaedwinPlayer;
import me.george.daedwin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Timestamp;

public class LogoutManager {

    public static void handleLogout(Player player, String message) {
        if (player == null) return;

        Utils.log.info("Handling logout for " + player.getName() + " (" + player.getUniqueId().toString() + ")");

        DaedwinPlayer wrapper = DaedwinPlayer.getDaedwinPlayers().get(player.getUniqueId());

        if (wrapper == null) {
            Bukkit.getLogger().info("null player wrapper for " + player.getName() + " during logout");
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Daedwin.getInstance(), () -> {
            // Handle general unregistering of stuff here

            if (Daedwin.getInstance()._hiddenPlayers.contains(player))
                Daedwin.getInstance()._hiddenPlayers.remove(player);

            DatabaseAPI.savePlayer(player.getUniqueId());

            Utils.log.info("Saved information for name/uuid: " + player.getName() + "/" + player.getUniqueId().toString() + " on their logout.");
        });

        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());

        wrapper.setLastLogout(timeStamp);
        wrapper.setPlayingStatus(false);

        DaedwinPlayer.getDaedwinPlayers().remove(player.getUniqueId());

        player.kickPlayer(message == null ? "Disconnected" : message);
    }

    public static void handleLogout(Player player) {
        if (player == null ) return;

        Utils.log.info("Handling logout for " + player.getName() + " (" + player.getUniqueId().toString() + ")");

        DaedwinPlayer wrapper = DaedwinPlayer.getDaedwinPlayers().get(player.getUniqueId());

        if (wrapper == null) {
            Bukkit.getLogger().info("Null player wrapper for " + player.getName() + " during logout.");
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Daedwin.getInstance(), () -> {
            // Handle general unregistering of stuff here and save player data

            if (Daedwin.getInstance()._hiddenPlayers.contains(player))
                Daedwin.getInstance()._hiddenPlayers.remove(player);

            DatabaseAPI.savePlayer(player.getUniqueId());

            Utils.log.info("Saved information for name/uuid: " + player.getName() + "/" + player.getUniqueId().toString() + " on their logout.");
        });

        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());

        wrapper.setLastLogout(timeStamp);
        wrapper.setPlayingStatus(false);

        DaedwinPlayer.getDaedwinPlayers().remove(player.getUniqueId());

        player.kickPlayer("Disconnected");
    }
}
