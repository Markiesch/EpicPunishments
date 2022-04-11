package com.markiesch.controllers;

import com.markiesch.EpicPunishments;
import com.markiesch.models.ProfileModel;

import java.util.UUID;

public class ProfileController {
    private final EpicPunishments plugin;

    public ProfileController(EpicPunishments plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates a profile for the given player, if there is already a profile present that will be returned
     * @param uuid the UUID of the player
     * @return the newly created profile
     */
    public ProfileModel createProfile(UUID uuid) {
        ProfileModel profile = new ProfileModel(plugin, uuid);
        plugin.getStorage().createProfile(profile);

        return profile;
    }
}
