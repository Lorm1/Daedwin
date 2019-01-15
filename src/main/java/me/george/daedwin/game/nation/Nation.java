package me.george.daedwin.game.nation;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Nation {
    HUMAN("Human", ChatColor.GRAY, new Location(Bukkit.getWorld("world"), 0, 0, 0)),
    ELF("Elf" , ChatColor.DARK_GREEN, new Location(Bukkit.getWorld("world"), 0, 0, 0)),
    VIKING("Viking", ChatColor.BLUE, new Location(Bukkit.getWorld("world"), 0, 0, 0)),
    ORC("Orc", ChatColor.DARK_RED, new Location(Bukkit.getWorld("world"), 0, 0, 0));

    @Getter String name;
    @Getter ChatColor color;
    @Getter Location spawnLocation;

    Nation(String name, ChatColor color, Location spawnLocation) {
        this.name = name;
        this.color = color;
        this.spawnLocation = spawnLocation;
    }

    public String getPrefix() {
        return color + name;
    }

    public static ChatColor getColor(Nation nation) {
        switch (nation) {
            case HUMAN:
                return ChatColor.GRAY;
            case ELF:
                return ChatColor.DARK_GREEN;
            case VIKING:
                return ChatColor.BLUE;
            case ORC:
                return ChatColor.DARK_RED;
            default:
                return ChatColor.GRAY;
        }
    }

    public static boolean isValidNation(final String nation) {
        return Arrays.stream(Nation.values())
                .map(Nation::name)
                .collect(Collectors.toSet())
                .contains(nation);
    }
}
