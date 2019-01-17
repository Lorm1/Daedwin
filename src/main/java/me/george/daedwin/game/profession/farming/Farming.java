package me.george.daedwin.game.profession.farming;

import me.george.daedwin.Daedwin;
import me.george.daedwin.game.profession.farming.enums.FarmableBlock;
import me.george.daedwin.game.profession.farming.enums.FarmingItem;
import me.george.daedwin.utils.Countdown;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Farming implements Listener {

    Set<UUID> playersFarming = new HashSet<>();

    public boolean isCounting = false;

    public void doFarm(FarmableBlock farmableBlock, Block block, Player p) {
        if (!playersFarming.contains(p.getUniqueId())) {
            if (block.getRelative(BlockFace.UP).getType() == Material.SUGAR_CANE || block.getRelative(BlockFace.UP).getType() == Material.GREEN_STAINED_GLASS) {
                p.sendMessage(ChatColor.RED + "You cannot farm this Sugar Cane."); // the block above this cane, is either a sugarcane or a farmed cane (glass block).
                return;
            }

            playersFarming.add(p.getUniqueId());
            isCounting = true;

            new Countdown(farmableBlock.getFarmingTime(), Daedwin.getInstance()) {
                @Override
                public void count(int current) {
                    Location location = p.getLocation();

                    if (!playersFarming.contains(p.getUniqueId())) {
                        this.task.cancel();

                        isCounting = false;
                        return;
                    }

                    if (current == 0) {
                        isCounting = false;
                        playersFarming.remove(p.getUniqueId());

                        p.sendMessage(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + ">> " + ChatColor.GREEN + "You have farmed a " +
                                farmableBlock.getFarmingReward().getName());
                        p.getInventory().addItem(farmableBlock.getFarmingReward().getItemStack());
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 3);

                        block.getWorld().playEffect(block.getLocation(), Effect.VILLAGER_PLANT_GROW, 5, 5);
                        addCooldown(farmableBlock, block);

                        this.task.cancel();
                        return;
                    }

                    if (current > 0 && playersFarming.contains(p.getUniqueId())) {
                        isCounting = true;

                        if (current > 0) {
                            p.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + ">> " + ChatColor.GREEN + "" +
                                    ChatColor.BOLD + "Farming... " + ChatColor.GOLD + "" + current + ChatColor.GREEN + " seconds left.");
                        }

                        block.getWorld().playEffect(block.getLocation(), Effect.VILLAGER_PLANT_GROW, 5, 5);
                        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_GRASS_BREAK, 5, 5);
                    }

                    if (p.getLocation().distance(location) >= 4) {
                        this.task.cancel();
                        isCounting = false;

                        playersFarming.remove(p.getUniqueId());

                        p.playEffect(p.getLocation(), Effect.SMOKE, 1);
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 3F, 3F);
                        p.sendMessage(ChatColor.RED.toString() + ChatColor.UNDERLINE + "Farming cancelled.");
                    }
                }
            }.start();
        } else {
            p.sendMessage(ChatColor.RED + "You are already farming.");
            return;
        }
    }

    public void addCooldown(FarmableBlock farmableBlock, Block block) {
        block.setType(farmableBlock.getChangeTo());
        Bukkit.getScheduler().scheduleSyncDelayedTask(Daedwin.getInstance(), () -> {
            block.setType(farmableBlock.getMaterial());
            block.getWorld().playEffect(block.getLocation().add(0, 2.5, 0), Effect.VILLAGER_PLANT_GROW, 1);
        }, 20 * farmableBlock.getRespawnTime());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action action = e.getAction();
        Block block = e.getClickedBlock();

        if (action == Action.LEFT_CLICK_BLOCK) {
            if (FarmableBlock.isFarmable(block)) {
                if (p.getGameMode().equals(GameMode.CREATIVE)) return;
                FarmableBlock farmableBlock = FarmableBlock.valueOf(block.getType().name());
                if (FarmableBlock.canFarmUsing(farmableBlock, FarmingItem.valueOf(p.getInventory().getItemInMainHand().getType().name()))) {
                    doFarm(farmableBlock, block, p);
                } else {
                    p.sendMessage(ChatColor.RED + "You cannot farm this.");
                }
            }
            return;
        }
    }
}
