package com.markiesch.modules.profile;

import com.markiesch.modules.infraction.InfractionController;
import com.markiesch.modules.infraction.InfractionModel;
import com.markiesch.modules.infraction.InfractionType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProfileModel {
    public final UUID uuid;

    public ProfileModel(UUID uuid) {
        this.uuid = uuid;
    }

    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public List<InfractionModel> getInfractions() {
        return new InfractionController().readAll(uuid);
    }

    public InfractionModel getActiveInfraction(InfractionType type) {
        return getInfractions()
                .stream()
                .filter(infraction -> infraction.type == type)
                .filter(InfractionModel::isActive)
                .findFirst()
                .orElse(null);
    }

    public List<InfractionModel> getActiveInfractions(InfractionType type) {
        return getInfractions()
                .stream()
                .filter(infraction -> infraction.type == type)
                .filter(InfractionModel::isActive)
                .collect(Collectors.toList());
    }

    public boolean isBanned() {
        return (getActiveInfractions(InfractionType.BAN).size() > 0);
    }

    public boolean isMuted() {
        return (getActiveInfractions(InfractionType.MUTE).size() > 0);
    }
}
