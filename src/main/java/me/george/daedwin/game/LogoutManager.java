package me.george.daedwin.game;

import me.george.daedwin.Daedwin;
import me.george.daedwin.database.DatabaseAPI;
import me.george.daedwin.game.player.DaedwinPlayer;
import me.george.daedwin.utils.Utils;
import org.bukkit.Bukkit;

public class LogoutManager {

    public static void handleLogout(DaedwinPlayer player, String message) {
        if (player == null) return;

        DaedwinPlayer wrapper = DaedwinPlayer.getDaedwinPlayers().get(player.getPlayer().getUniqueId());

        if (wrapper == null) {
            Bukkit.getLogger().info("Null player wrapper for " + player.getPlayer().getName() + " during logout");
            return;
        }

        if (Daedwin.getInstance()._hiddenPlayers.contains(player))
            Daedwin.getInstance()._hiddenPlayers.remove(player);

        Bukkit.getScheduler().runTaskAsynchronously(Daedwin.getInstance(), () -> {
            // Handle general unregistering of stuff here

            DatabaseAPI.savePlayer(wrapper);
        });

//        DaedwinPlayer.getDaedwinPlayers().remove(wrapper.getPlayer().getUniqueId(), wrapper);

        player.getPlayer().kickPlayer(message == null || message.equals("") ? "Disconnected" : message);

        Utils.log.info("Handled Logout for Player " + player.getPlayer().getName());
    }

    public static void handleLogout(DaedwinPlayer player) {
        if (player == null ) return;

        DaedwinPlayer wrapper = DaedwinPlayer.getDaedwinPlayers().get(player.getPlayer().getUniqueId());

        if (wrapper == null) {
            Bukkit.getLogger().info("Null player wrapper for " + player.getPlayer().getName() + " during logout.");
            return;
        }

        if (Daedwin.getInstance()._hiddenPlayers.contains(player))
            Daedwin.getInstance()._hiddenPlayers.remove(player);

        Bukkit.getScheduler().runTaskAsynchronously(Daedwin.getInstance(), () -> {
            // Handle general unregistering of stuff here and save player data

            DatabaseAPI.savePlayer(wrapper);
        });

//        DaedwinPlayer.getDaedwinPlayers().remove(wrapper.getPlayer().getUniqueId(), wrapper);

        player.getPlayer().kickPlayer("Disconnected");

        Utils.log.info("Handled Logout for Player " + player.getPlayer().getName());
    }
}
