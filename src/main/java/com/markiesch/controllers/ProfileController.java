package com.markiesch.controllers;

import com.markiesch.EpicPunishments;
import com.markiesch.models.ProfileModel;

import java.util.HashMap;
import java.util.UUID;

public class ProfileController {
    private final EpicPunishments plugin;
    private final HashMap<UUID, ProfileModel> profiles;

    public ProfileController(EpicPunishments plugin) {
        this.plugin = plugin;
        profiles = new HashMap<>();
    }

    /**
     * Creates a profile for the given player, if there is already a profile present that will be returned
     * @param uuid the UUID of the player
     * @return the newly created profile
     */
    public ProfileModel createProfile(UUID uuid) {
        if (isPlayerRegistered(uuid)) return getPlayer(uuid);

        ProfileModel profile = new ProfileModel(plugin, uuid);
        plugin.getStorage().createProfile(profile);
        profiles.put(uuid, profile);

        return profile;
    }

    public ProfileModel getProfile(UUID uuid) {
        return profiles.get(uuid);
    }

    public boolean isPlayerRegistered(UUID uuid) {
        return profiles.containsKey(uuid);
    }

    public ProfileModel getPlayer(UUID uuid) {
        return profiles.get(uuid);
    }
}
