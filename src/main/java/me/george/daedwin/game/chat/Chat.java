package me.george.daedwin.game.chat;

import me.george.daedwin.Daedwin;
import me.george.daedwin.game.rank.Rank;
import me.george.daedwin.game.player.DaedwinPlayer;
import me.george.daedwin.utils.ChatUtils;
import me.george.daedwin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.Set;

public class Chat implements Listener {

    public static volatile boolean chatEnabled = true;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        e.setCancelled(true);

        Rank rank = player.getRank();

        if (player.getIsMuted()) {
            Bukkit.getScheduler().runTaskAsynchronously(Daedwin.getInstance(), () -> {
                if(Daedwin.getInstance().getPunishmentManager().getMuteManager().isMuted(p.getUniqueId())){
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.UNDERLINE + "You are muted.");
                    p.sendMessage("");
                    p.sendMessage(ChatColor.BLUE.toString() + "Reason" + ChatColor.BLUE + ": " + ChatColor.GRAY + Daedwin.getInstance().getPunishmentManager().getMuteManager().getReason(p.getUniqueId()));
                    p.sendMessage(ChatColor.GOLD.toString() + "Expires" + ChatColor.GOLD + ": " + ChatColor.DARK_RED + Daedwin.getInstance().getPunishmentManager().getMuteManager().getTimeLeft(p.getUniqueId()));
                }
            });
            return;
        }

        String message = e.getMessage();

        if (ChatUtils.containsIllegal(message)) {
            p.sendMessage(ChatColor.RED + "Your message contained invalid characters.");
            return;
        }

        if (message.length() > 69) {
            p.sendMessage(ChatColor.RED + "Your message was too long. Maximum Characters: 70");
            return;
        }

        String checkedMessage = ChatUtils.checkForBannedWords(message);

        String formattedMessage = rank.getPrefix() + " " + ChatColor.GRAY + player.getPlayer().getDisplayName() + ": " + ChatColor.WHITE + checkedMessage;

        Set<Player> nearbyPlayers = new HashSet<Player>();

        for (Player players : Utils.getNearbyPlayers(p.getLocation(), 75)) {
            nearbyPlayers.add(players);

            for (Player pl : nearbyPlayers) {
                pl.sendMessage();
            }

            p.sendMessage(formattedMessage);

            if (nearbyPlayers.contains(p)) {
                nearbyPlayers.remove(p);
            }

            if (nearbyPlayers.isEmpty()) {
                p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "No one heard you...");
                p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 5, 5);
            }
        }

        Utils.log.info("CHAT: " + formattedMessage);
    }
}
