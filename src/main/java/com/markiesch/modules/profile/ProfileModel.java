package com.markiesch.modules.profile;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class ProfileModel {
    public final UUID uuid;

    public ProfileModel(UUID uuid) {
        this.uuid = uuid;
    }

    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }
}
