package me.george.daedwin.game.world;

import me.george.daedwin.game.commands.CommandFreeze;
import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.InventoryHolder;

public class Restrictions implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void chat(PlayerChatTabCompleteEvent event) {
        Player p = event.getPlayer();
        DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        String msg = event.getChatMessage();

        if (!player.isAdmin()) {
            if (msg.contains("/")) {
                event.getTabCompletions().clear();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        if ((!p.isOp() || player.isAdmin()) && !p.getGameMode().equals(GameMode.CREATIVE)) { // only admins in creative can modify the world.
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        Action action = e.getAction();
        Block block = e.getClickedBlock();

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (p.getItemInHand().getType() == null || p.getItemInHand() == null) return;

            if (!player.isAdmin() && !p.getGameMode().equals(GameMode.CREATIVE)) {
                if (block.getState() instanceof InventoryHolder) {
                    e.setCancelled(true);
                }

                if (p.getItemInHand().getType() == Material.SHEARS)
                    e.setCancelled(true);

                if (p.getItemInHand().getType() == Material.ENDER_PEARL)
                    e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemSwap(PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();

        if (e.getMainHandItem() == null || e.getMainHandItem().getType() == null || e.getMainHandItem().getType() == Material.AIR || p.getItemInHand() == null || p.getItemInHand().getType() == null)
            return;

        if (e.getMainHandItem().getType() == Material.DIAMOND_SWORD || e.getMainHandItem().getType() == Material.DIAMOND_AXE || e.getMainHandItem().getType() == Material.IRON_AXE || e.getMainHandItem().getType() == Material.IRON_SWORD) {
            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 5, 5);
        }
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent e) {
        Player p = e.getPlayer();
        DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        if (!player.isAdmin()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent e) {
        e.setCancelled(true); // stops block from fading = stop soiled dirt turning into normal dirt (by jumping on it etc...)
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if (CommandFreeze.frozen.contains(p)) {
            e.setCancelled(true);

            p.sendMessage(ChatColor.BLUE + "You cannot move while frozen.");
        }
    }
}
