package com.markiesch.utils.configs;

import com.markiesch.EpicPunishments;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class Config {
    protected abstract String getResource();

    private static EpicPunishments plugin;
    private static FileConfiguration dataConfig = null;
    private final File configFile;

    public Config(EpicPunishments instance) {
        plugin = instance;
        configFile = new File(plugin.getDataFolder(), getResource());
        saveDefaultConfig();
    }

    public void reloadConfig() {
        dataConfig = YamlConfiguration.loadConfiguration(configFile);
        InputStream defaultStream = plugin.getResource(getResource());

        if (defaultStream == null) return;
        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
        dataConfig.setDefaults(defaultConfig);
    }

    public FileConfiguration getConfig() {
        if (dataConfig == null) reloadConfig();
        return dataConfig;
    }

    public void saveDefaultConfig() {
        if (!configFile.exists()) plugin.saveResource(getResource(), false);
    }

    public String getString(String string) {
        return getConfig().getString(string);
    }
}
