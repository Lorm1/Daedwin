package me.george.daedwin.game.profession.farming.enums;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public enum FarmingItem {
    WOODEN_HOE(new ItemStack(Material.WOODEN_HOE), ChatColor.WHITE + "Novice Sickle", Arrays.asList(ChatColor.GRAY.toString() +
            ChatColor.ITALIC + "A rusty wooden sickle.")),
    STONE_HOE(new ItemStack(Material.STONE_HOE), ChatColor.GREEN + "Apprentice Sickle", Arrays.asList(ChatColor.GRAY.toString() +
            ChatColor.ITALIC + "A hard stone sickle.")),
    IRON_HOE(new ItemStack(Material.IRON_HOE), ChatColor.AQUA + "Advanced Sickle", Arrays.asList(ChatColor.GRAY.toString() +
            ChatColor.ITALIC + "A sharp iron sickle.")),
    DIAMOND_HOE(new ItemStack(Material.DIAMOND_HOE), ChatColor.LIGHT_PURPLE + "Veteran Sickle", Arrays.asList(ChatColor.GRAY.toString() +
            ChatColor.ITALIC + "A shiny diamond sickle.")),
    GOLDEN_HOE(new ItemStack(Material.GOLDEN_HOE), ChatColor.YELLOW + "Master Sickle", Arrays.asList(ChatColor.GRAY.toString() +
            ChatColor.ITALIC + "A finely forged golden sickle."));

    @Getter ItemStack itemStack;
    @Getter String name;
    @Getter List<String> lore;

    FarmingItem(ItemStack itemStack, String name, List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        this.itemStack = itemStack;
        this.name = name;
        this.lore = lore;
    }
}
