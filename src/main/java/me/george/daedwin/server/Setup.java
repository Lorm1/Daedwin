package me.george.daedwin.server;

import me.george.daedwin.Constants;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class Setup implements Listener {

    public static boolean MAINTENANCE_MODE = false;

    @EventHandler
    public void onPing(ServerListPingEvent e) {
        if (!MAINTENANCE_MODE) {
            e.setMotd(ChatColor.translateAlternateColorCodes('&', Constants.MOTD));
        } else {
            e.setMotd(ChatColor.translateAlternateColorCodes('&', Constants.MAINTENANCE_MOTD));
        }

        e.setMaxPlayers(Constants.PLAYER_SLOTS);
    }
}
