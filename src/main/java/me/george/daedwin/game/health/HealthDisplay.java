package me.george.daedwin.game.health;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class HealthDisplay {

    public static ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
    public static Scoreboard s = scoreboardManager.getNewScoreboard();

    public static void setup() {
        // health below name
        Objective o = s.registerNewObjective("healthnamebar", "dummy");

        o.setDisplaySlot(DisplaySlot.BELOW_NAME);
        o.setDisplayName("%");

        for (Player players : Bukkit.getOnlinePlayers()) {
            if (!(players == null)) {
                players.setScoreboard(s);

                Double health = players.getHealth();
                players.setHealth(health);

                o.getScore(players.getName()).setScore(health.intValue() * 5);
            }
        }

        // bossbar
        for (Player p : Bukkit.getOnlinePlayers()) {
            BossBar bar = Bukkit.createBossBar(ChatColor.RED + "HP " + p.getHealth() + " / " + p.getMaxHealth(), BarColor.GREEN, BarStyle.SOLID);
            bar.addPlayer(p);
        }
    }
}
