package me.george.daedwin.game.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommand implements Listener {

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
