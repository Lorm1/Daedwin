package me.george.daedwin.game.profession.farming.enums;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public enum FarmingReward {
    MELON(new ItemStack(Material.MELON_SLICE), ChatColor.LIGHT_PURPLE + "Melon Slice", Arrays.asList(ChatColor.GRAY.toString() +
            ChatColor.ITALIC + "A freshly cut melon slice.")),
    PUMPKIN(new ItemStack(Material.PUMPKIN_PIE), ChatColor.GOLD + "Pumpkin Slice", Arrays.asList(ChatColor.GRAY.toString() +
            ChatColor.ITALIC + "A freshly cut pumpkin slice.")),
    WHEAT(new ItemStack(Material.WHEAT), ChatColor.YELLOW + "Wheat", Arrays.asList(ChatColor.GRAY.toString() +
            ChatColor.ITALIC + "A freshly cut bunch of wheat.")),
    CARROT(new ItemStack(Material.CARROT), ChatColor.RED + "Carrot", Arrays.asList(ChatColor.GRAY.toString() +
            ChatColor.ITALIC + "A freshly cut carrot.")),
    SUGAR_CANE(new ItemStack(Material.SUGAR_CANE), ChatColor.GREEN + "Sugar Cane", Arrays.asList(ChatColor.GRAY.toString() +
            ChatColor.ITALIC + "A freshly cut sugar cane."));

    @Getter ItemStack itemStack;
    @Getter String name;
    @Getter List<String> lore;

    FarmingReward(ItemStack itemStack, String name, List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        this.itemStack = itemStack;
        this.name = name;
        this.lore = lore;
    }
}
