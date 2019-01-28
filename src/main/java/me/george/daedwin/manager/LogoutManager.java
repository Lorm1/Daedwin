package me.george.daedwin.manager;

import me.george.daedwin.Daedwin;
import me.george.daedwin.database.DatabaseAPI;
import me.george.daedwin.game.player.DaedwinPlayer;
import me.george.daedwin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LogoutManager {

    public static void handleLogout(Player player, String message) {
        if (player == null) return;

        DaedwinPlayer daedwinPlayer = DaedwinPlayer.getInstanceOfPlayer(player);

        if (daedwinPlayer == null) {
            Bukkit.getLogger().info("Null player wrapper for " + player.getName() + " during logout");
            return;
        }

        if (Daedwin.getInstance()._hiddenPlayers.contains(player))
            Daedwin.getInstance()._hiddenPlayers.remove(player);

        Bukkit.getScheduler().runTaskAsynchronously(Daedwin.getInstance(), () -> {
            // Handle general unregistering of stuff here and save player data as well. (Nothing to unregister atm)
            DatabaseAPI.savePlayer(daedwinPlayer);
        });

        DaedwinPlayer.getDaedwinPlayers().remove(daedwinPlayer.getPlayer().getUniqueId(), daedwinPlayer);

        player.getPlayer().kickPlayer(message == null || message.equals("") ? "Disconnected" : message);

        Utils.log.info("Handled Logout for Player " + daedwinPlayer.getPlayer().getName());
    }

    public static void handleLogout(Player player) {
        if (player == null ) return;

        DaedwinPlayer daedwinPlayer = DaedwinPlayer.getInstanceOfPlayer(player);

        if (daedwinPlayer == null) {
            Bukkit.getLogger().info("Null player wrapper for " + player.getName() + " during logout.");
            return;
        }

        if (Daedwin.getInstance()._hiddenPlayers.contains(player))
            Daedwin.getInstance()._hiddenPlayers.remove(player);

        Bukkit.getScheduler().runTaskAsynchronously(Daedwin.getInstance(), () -> {
            // Handle general unregistering of stuff here and save player data as well. (Nothing to unregister atm)
            DatabaseAPI.savePlayer(daedwinPlayer);
        });

        DaedwinPlayer.getDaedwinPlayers().remove(daedwinPlayer.getPlayer().getUniqueId(), daedwinPlayer);

        player.getPlayer().kickPlayer("Disconnected");

        Utils.log.info("Handled Logout for Player " + player.getName());
    }
}
