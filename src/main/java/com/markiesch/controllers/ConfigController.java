package com.markiesch.controllers;

import com.markiesch.EpicPunishments;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class ConfigController {
    private static final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);
    private static FileConfiguration dataConfig = null;
    private final File configFile;
    private final String fileName;

    protected ConfigController(String fileName) {
        this.fileName = fileName;
        configFile = new File(plugin.getDataFolder(), fileName);
        saveDefaultConfig();
    }

    public void reloadConfig() {
        // Create the data file if it doesn't exist already
        dataConfig = YamlConfiguration.loadConfiguration(configFile);
        InputStream defaultStream = plugin.getResource(fileName);

        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (dataConfig == null) reloadConfig();
        return dataConfig;
    }

    protected void saveConfig() {
        if (dataConfig == null || configFile == null) return;

        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, e);
        }
    }

    protected void saveDefaultConfig() {
        if (!configFile.exists()) plugin.saveResource(fileName, false);
    }

    public ConfigurationSection getConfigurationSection(String uuid) {
        return getConfig().getConfigurationSection(uuid);
    }
}
