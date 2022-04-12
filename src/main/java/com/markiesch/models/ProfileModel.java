package com.markiesch.models;

import com.markiesch.EpicPunishments;
import com.markiesch.utils.PunishTypes;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public class ProfileModel {
    private final EpicPunishments plugin;
    public final UUID uuid;

    public ProfileModel(EpicPunishments plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public List<InfractionModel> getInfractions() {
        return plugin.getInfractionController().readAll(uuid);
    }

    public InfractionModel getActiveInfraction(PunishTypes type) {
        return getInfractions()
                .stream()
                .filter(InfractionModel::isActive)
                .findFirst()
                .orElse(null);
    }

    public boolean isBanned() {
        return getActiveInfraction(PunishTypes.BAN) != null;
    }
}
