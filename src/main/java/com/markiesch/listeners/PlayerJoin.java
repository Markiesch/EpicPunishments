package com.markiesch.listeners;

import com.markiesch.modules.infraction.InfractionList;
import com.markiesch.modules.infraction.InfractionManager;
import com.markiesch.modules.infraction.InfractionModel;
import com.markiesch.modules.infraction.InfractionType;
import com.markiesch.modules.profile.ProfileController;
import com.markiesch.modules.profile.ProfileModel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class PlayerJoin implements Listener {
    private final ProfileController profileController;

    public PlayerJoin() {
        profileController = new ProfileController();
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        profileController.createProfile(uuid, event.getAddress().getHostName());

        ProfileModel profile = profileController.getProfile(uuid);

        if (profile == null) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Your profile couldn't be created or found, please rejoin");
            return;
        }

        InfractionList infractions = InfractionManager.getInstance().getPlayer(uuid);
        InfractionModel activeBan = infractions.getActiveByType(InfractionType.BAN)
                .stream()
                .findFirst()
                .orElse(null);

        if (activeBan == null) return;

        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, activeBan.reason);
    }
}
