package com.markiesch.listeners;

import com.markiesch.EpicPunishments;
import com.markiesch.controllers.ProfileController;
import com.markiesch.models.ProfileModel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class PlayerJoin implements Listener {
    private final EpicPunishments plugin;

    public PlayerJoin(EpicPunishments plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        ProfileController profileController = plugin.getProfileController();

        ProfileModel profile = profileController.createProfile(uuid);

        if (profile.isBanned()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, "The ban hammer has spoken...");
        }
    }
}
