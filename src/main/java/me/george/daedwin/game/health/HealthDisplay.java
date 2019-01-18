package me.george.daedwin.game.health;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class HealthDisplay {

    public static ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
    public static Scoreboard s = scoreboardManager.getNewScoreboard();

    public static void setup() {
        Objective o = s.registerNewObjective("healthbar", "dummy");

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
    }
}
