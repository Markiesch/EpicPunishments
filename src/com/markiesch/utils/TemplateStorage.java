package com.markiesch.utils;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getServer;


public class TemplateStorage {
    private final EpicPunishments plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public TemplateStorage(EpicPunishments plugin) {
        this.plugin = plugin;
        // saves/initializes the config
        saveDefaultConfig();
    }

    public void reloadConfig() {
        // Create the data file if it doesn't exist already
        if (configFile == null) configFile = new File(plugin.getDataFolder(), "templates.yml");
        dataConfig = YamlConfiguration.loadConfiguration(configFile);
        InputStream defaultStream = plugin.getResource("templates.yml");

        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (dataConfig == null) reloadConfig();
        return dataConfig;
    }

    public void saveConfig() {
        if (dataConfig == null || configFile == null) return;

        try {
            getConfig().save(this.configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, e);
        }
    }

    public void saveDefaultConfig() {
        if (configFile == null) configFile = new File(plugin.getDataFolder(), "templates.yml");
        if (!configFile.exists()) plugin.saveResource("templates.yml", false);
    }

    public void addTemplate(String type, Player player) {
        PlayerMenuUtility playerMenuUtility = EpicPunishments.getPlayerMenuUtility(player);
        String name = playerMenuUtility.getTemplateName();
        String reason = playerMenuUtility.getReason();

        getConfig().set(name + ".type", type);
        getConfig().set(name + ".reason", reason);
        saveConfig();
    }

    public void removeTemplate(String template) {
        if (template == null) return;
        template = ChatColor.stripColor(template);
        getConfig().set(template, null);
        saveConfig();
    }
}
