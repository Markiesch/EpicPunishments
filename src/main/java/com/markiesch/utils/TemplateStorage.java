package com.markiesch.utils;

import com.markiesch.EpicPunishments;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.logging.Level;

public class TemplateStorage {
    private static final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);
    private static FileConfiguration dataConfig = null;
    static File configFile = new File(plugin.getDataFolder(), "templates.yml");

    public static void reloadConfig() {
        // Create the data file if it doesn't exist already
        dataConfig = YamlConfiguration.loadConfiguration(configFile);
        InputStream defaultStream = plugin.getResource("templates.yml");

        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            dataConfig.setDefaults(defaultConfig);
        }
    }

    public static FileConfiguration getConfig() {
        if (dataConfig == null) reloadConfig();
        return dataConfig;
    }

    public static void saveConfig() {
        if (dataConfig == null || configFile == null) return;

        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, e);
        }
    }

    public static void saveDefaultConfig() {
        if (!configFile.exists()) plugin.saveResource("templates.yml", false);
    }

    public static void addTemplate(String name, String reason, String type, long duration) {
        UUID uuid = UuidUtil.getTimeBasedUuid();
        ConfigurationSection section = getConfig().createSection(uuid.toString());
        section.set("name", name);
        section.set("type", type);
        section.set("duration", duration);
        section.set("reason", reason);
        saveConfig();
    }

    public static boolean editTemplate(UUID uuid, String name, String reason, String type, long duration) {
        ConfigurationSection section = getConfig().getConfigurationSection(uuid.toString());
        if (section == null) return false;

        section.set("name", name);
        section.set("type", type);
        section.set("duration", duration);
        section.set("reason", reason);

        saveConfig();
        return true;
    }

    public static boolean removeTemplate(UUID uuid) {
        ConfigurationSection section = getConfig().getConfigurationSection(uuid.toString());
        if (section == null) return false;
        getConfig().set(uuid.toString(), null);
        saveConfig();
        return true;
    }

    public static UUID getUUIDFromName(String name) {
        ConfigurationSection section = getConfig().getConfigurationSection("");
        if (section == null) return null;

        UUID templateUuid = null;
        for (String uuid : section.getKeys(false)) {
            if (name.equals(getConfig().getString(uuid + ".name"))) templateUuid = UUID.fromString(uuid);
        }

        return templateUuid;
    }
}
