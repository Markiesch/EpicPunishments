package com.markiesch.utils;

import com.google.common.base.Charsets;
import com.markiesch.EpicPunishments;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

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
        try (InputStream defaultStream = plugin.getResource(getResource())) {
            if (defaultStream == null) return;
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream, Charsets.UTF_8));

            dataConfig = YamlConfiguration.loadConfiguration(configFile);
            dataConfig.setDefaults(defaultConfig);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public FileConfiguration getConfig() {
        if (dataConfig == null) reloadConfig();
        return dataConfig;
    }

    private void saveDefaultConfig() {
        if (!configFile.exists()) plugin.saveResource(getResource(), false);
    }

    public String getString(String string) {
        return getConfig().getString(string);
    }

    public List<String> getStringList(String string) {
        return getConfig().getStringList(string);
    }
}