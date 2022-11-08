package com.markiesch.listeners;

import com.markiesch.EpicPunishments;
import com.markiesch.modules.profile.ProfileController;
import com.markiesch.modules.profile.ProfileModel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerJoin implements Listener {
    private final EpicPunishments plugin;
    private final ProfileController profileController;

    public PlayerJoin(EpicPunishments plugin) {
        this.plugin = plugin;
        profileController = new ProfileController();
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

        profileController.createProfile(uuid, event.getAddress().getHostName());

        ProfileModel profile = profileController.getProfile(uuid);

        if (profile == null) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "You profile couldn't be created or found, please rejoin");
            return;
        }

        if (profile.isBanned()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, "The ban hammer has spoken...");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        
        plugin.playerMenuUtilityMap.remove(event.getPlayer().getUniqueId());
    }
}
