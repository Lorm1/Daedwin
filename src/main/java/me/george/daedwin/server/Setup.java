package me.george.daedwin.server;

import me.george.daedwin.Constants;
import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
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

    @EventHandler
    public void onComamnd(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String command = e.getMessage();

        for (Player player : Bukkit.getOnlinePlayers()) {
            DaedwinPlayer pl = DaedwinPlayer.getDaedwinPlayers().get(player.getUniqueId());
            if (pl.isStaff()) {
                pl.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + p.getName() + ": " + ChatColor.RED + command);
            }
        }
    }
}
