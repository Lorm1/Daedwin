package me.george.daedwin.game.punishment;

import lombok.Getter;
import me.george.daedwin.Daedwin;
import me.george.daedwin.game.punishment.ban.BanManager;
import me.george.daedwin.game.punishment.mute.MuteManager;

public class PunishmentManager {

    public PunishmentManager(Daedwin daedwin) {
        setupManagers();
    }

    @Getter BanManager banManager;
    @Getter MuteManager muteManager;

    public void setupManagers() {
        banManager = new BanManager(this);
        muteManager = new MuteManager(this);
    }
}
