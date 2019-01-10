package me.george.daedwin.game.rank;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Rank {
    ADMIN(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "ADMIN"),
    MOD(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "MOD"),
    DONATOR(ChatColor.GRAY.toString()),
    TESTER(ChatColor.WHITE.toString() + ChatColor.BOLD + "TESTER"),
    DEFAULT(ChatColor.GRAY.toString());

    String prefix;

    Rank(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public static ChatColor getColor(Rank rank) {
        switch (rank) {
            case ADMIN:
                return ChatColor.DARK_RED;
            case MOD:
                return ChatColor.DARK_PURPLE;
            case DONATOR:
                return ChatColor.GREEN;
            case TESTER:
                return ChatColor.WHITE;
            case DEFAULT:
                return ChatColor.GRAY;
            default:
                return ChatColor.GRAY;
        }
    }

    public static boolean isValidRank(final String rank) {
        return Arrays.stream(Rank.values())
                .map(Rank::name)
                .collect(Collectors.toSet())
                .contains(rank);
    }
}
