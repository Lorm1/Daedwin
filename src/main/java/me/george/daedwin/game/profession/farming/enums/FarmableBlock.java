package me.george.daedwin.game.profession.farming.enums;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;

public enum FarmableBlock {
    MELON(Material.MELON, Material.LIME_STAINED_GLASS, FarmingReward.MELON, 5, 4),
    PUMPKIN(Material.PUMPKIN, Material.ORANGE_STAINED_GLASS, FarmingReward.PUMPKIN, 6, 6),
    WHEAT(Material.WHEAT, Material.YELLOW_STAINED_GLASS, FarmingReward.WHEAT, 8, 8),
    CARROT(Material.CARROT, Material.RED_STAINED_GLASS, FarmingReward.CARROT, 12, 10),
    SUGAR_CANE(Material.SUGAR_CANE, Material.GREEN_STAINED_GLASS, FarmingReward.SUGAR_CANE, 15, 10);

    @Getter Material material;
    @Getter Material changeTo;
    @Getter FarmingReward farmingReward;
    @Getter int farmingTime;
    @Getter int respawnTime;

    FarmableBlock(Material material, Material changeTo, FarmingReward farmingReward, int farmingTime, int respawnTime) {
        this.material = material;
        this.changeTo = changeTo;
        this.farmingReward = farmingReward;
        this.farmingTime = farmingTime;
        this.respawnTime = respawnTime;
    }

    public static boolean isFarmable(Block block) {
        for (FarmableBlock farm : FarmableBlock.values()) {
            if (farm.name().equals(block.getType().name())) {
                return true;
            }
        }
        return false;
    }

    public static boolean canFarmUsing(FarmableBlock block, FarmingItem item) {
        switch (block) {
            case MELON:
                if (item.equals(FarmingItem.WOODEN_HOE) || item.equals(FarmingItem.STONE_HOE) || item.equals(FarmingItem.IRON_HOE) || item.equals(FarmingItem.DIAMOND_HOE) ||
                        item.equals(FarmingItem.GOLDEN_HOE)) {
                    return true;
                }
                break;
            case PUMPKIN:
                if (item.equals(FarmingItem.STONE_HOE) || item.equals(FarmingItem.IRON_HOE) || item.equals(FarmingItem.DIAMOND_HOE) || item.equals(FarmingItem.GOLDEN_HOE)) {
                    return true;
                }
                break;
            case WHEAT:
                if (item.equals(FarmingItem.IRON_HOE) || item.equals(FarmingItem.DIAMOND_HOE) || item.equals(FarmingItem.GOLDEN_HOE)) {
                    return true;
                }
                break;
            case CARROT:
                if (item.equals(FarmingItem.DIAMOND_HOE) || item.equals(FarmingItem.GOLDEN_HOE)) {
                    return true;
                }
                break;
            case SUGAR_CANE:
                if (item.equals(FarmingItem.GOLDEN_HOE)) {
                    return true;
                }
                break;
        }
        return false;
    }
}
