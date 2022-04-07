package com.markiesch.controllers;

import com.markiesch.models.PlayerModel;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class PlayerController extends ConfigController{
    public PlayerController() {
        super("data.yml");
    }

    public List<PlayerModel> readAll() {
        List<PlayerModel> result = new ArrayList<>();

        ConfigurationSection configurationSection = getConfigurationSection("");
        if (configurationSection == null) return result;

        for (String templateUUID : configurationSection.getKeys(false)) {
            UUID uuid = UUID.fromString(templateUUID);

            PlayerModel player = new PlayerModel(uuid);
            result.add(player);
        }

        return result;
    }

    public void createPlayerProfile(UUID uuid) {
        if (getConfig().contains(uuid.toString())) return;
        List<String> emptyPunishments = new ArrayList<>();
        getConfig().set(uuid + ".infractions", emptyPunishments);
        saveConfig();

        getServer().getConsoleSender().sendMessage("§cSuccessfully created a new profile for " + Bukkit.getOfflinePlayer(uuid).getName());
    }
}
