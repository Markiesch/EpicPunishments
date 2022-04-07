package com.markiesch.models;

import com.markiesch.EpicPunishments;
import com.markiesch.controllers.TemplateController;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public class PlayerModel {
    private final TemplateController templateController;
    public final UUID uuid;

    public PlayerModel(UUID uuid) {
        this.uuid = uuid;
        templateController = EpicPunishments.getTemplateController();
    }

    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public boolean isPlayerRegistered() {
        return templateController.getConfigurationSection(uuid.toString()) != null;
    }

    public List<String> getPunishments() {
        return templateController.getConfig().getStringList(uuid + ".infractions");
    }

    public boolean isPlayerBanned(UUID uuid) {
        List<String> infractions = getPunishments();

        for (String infraction : infractions) {
            String type = infraction.split(";")[1];
            if (!"ban".equalsIgnoreCase(type)) continue;

            Long duration = Long.parseLong(infraction.split(";")[3]);
            if (duration.equals(0L)) return true;

            long currentTime = System.currentTimeMillis();
            long expires = Long.parseLong(infraction.split(";")[4]);
            if (currentTime < expires) return true;
        }

        return false;
    }
}
