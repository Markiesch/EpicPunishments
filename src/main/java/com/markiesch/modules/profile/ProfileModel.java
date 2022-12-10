package com.markiesch.modules.profile;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ProfileModel {
    public final UUID uuid;
    public final String name;
    public final String ip;
    public @Nullable String textureURL;

    public ProfileModel(UUID uuid, String name, String ip, @Nullable String textureURL) {
        this.uuid = uuid;
        this.name = name;
        this.ip = ip;
        this.textureURL = textureURL;
    }

    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public String getName() {
        return name;
    }
}
