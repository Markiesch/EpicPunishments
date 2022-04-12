package com.markiesch.listeners;

import com.markiesch.EpicPunishments;
import com.markiesch.controllers.ProfileController;
import com.markiesch.models.ProfileModel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class PlayerJoin implements Listener {
    private final ProfileController profileController;

    public PlayerJoin(EpicPunishments plugin) {
        this.profileController = plugin.getProfileController();
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

        profileController.createProfile(uuid);

        ProfileModel profile = profileController.getProfile(uuid);

        if (profile == null) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "You profile couldn't be created or found, please relog");
            return;
        }

        if (profile.isBanned()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, "The ban hammer has spoken...");
        }
    }
}
