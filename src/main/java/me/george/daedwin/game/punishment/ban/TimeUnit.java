package me.george.daedwin.game.punishment.ban;

import java.util.HashMap;

public enum TimeUnit {
    SECONDS("Seconds", "sec", 1),
    MINUTES("Minutes", "min", 60),
    HOURS("Hours", "h", 60 * 60),
    DAYS("Days", "d", 60 * 60 * 24),
    WEEKS("Weeks", "w", 60 * 60 * 24 * 7),
    MONTHS("Months", "m", 60 * 60 * 24 * 30);

    private String name;
    private String shortcut;
    private long toSecond;

    private static HashMap<String, TimeUnit> ID_SHORTCUT = new HashMap<>();

    TimeUnit(String name, String shortcut, long toSecond) {
        this.name = name;
        this.shortcut = shortcut;
        this.toSecond = toSecond;
    }

    static {
        for (TimeUnit unit : values()) {
            ID_SHORTCUT.put(unit.shortcut, unit);
        }
    }

    public static TimeUnit getFromShortcut(String shortcut) {
        return ID_SHORTCUT.get(shortcut);
    }

    public String getName() {
        return name;
    }

    public String getShortcut() {
        return shortcut;
    }

    public long getToSecond() {
        return toSecond;
    }

    public static boolean existFromShortcut(String shortcut) {
        return ID_SHORTCUT.containsKey(shortcut);
    }
}
