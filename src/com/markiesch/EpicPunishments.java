package com.markiesch;

import com.markiesch.commands.*;
import com.markiesch.listeners.*;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.utils.InputUtils;
import com.markiesch.utils.PlayerStorage;
import com.markiesch.utils.TemplateStorage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class EpicPunishments extends JavaPlugin implements Listener {
    public PlayerStorage PlayerStorage;
    public TemplateStorage TemplateStorage;
    private static EpicPunishments instance;

    public static EpicPunishments getInstance() {
        return instance;
    }

    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    private final Map<UUID, InputUtils> editor = new HashMap<>();

    public Map<UUID, InputUtils> getEditor() {
        return this.editor;
    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
        PlayerMenuUtility playerMenuUtility;

        if (playerMenuUtilityMap.containsKey(p)) {
            return playerMenuUtilityMap.get(p);
        } else {
            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUtilityMap.put(p, playerMenuUtility);
            return playerMenuUtility;
        }
    }

    public PlayerStorage getPlayerStorage() {
        return PlayerStorage;
    }
    public TemplateStorage getTemplateStorage() { return TemplateStorage; }

    public String changeColor(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }


    @Override
    public void onEnable() {
        this.PlayerStorage = new PlayerStorage(this);
        this.TemplateStorage = new TemplateStorage(this);
        instance = this;
        this.saveDefaultConfig();
        // Register commands and Listeners
        getServer().getPluginManager().registerEvents(new CommandSpy(), this);
        getServer().getPluginManager().registerEvents(new SignSpy(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInput(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        Objects.requireNonNull(getCommand("epicPunishments")).setExecutor(new PunishCommand());
        Objects.requireNonNull(getCommand("epicPunishments")).setTabCompleter(new PunishTabCompleter());
        Objects.requireNonNull(getCommand("ban")).setExecutor(new BanCommand());
        Objects.requireNonNull(getCommand("kick")).setExecutor(new KickCommand());
        Objects.requireNonNull(getCommand("mute")).setExecutor(new MuteCommand());
        Objects.requireNonNull(getCommand("template")).setExecutor(new TemplateCommand());
        Objects.requireNonNull(getCommand("ban")).setTabCompleter(new InfractionTabCompleter());
        Objects.requireNonNull(getCommand("kick")).setTabCompleter(new InfractionTabCompleter());
        Objects.requireNonNull(getCommand("mute")).setTabCompleter(new InfractionTabCompleter());
        Objects.requireNonNull(getCommand("template")).setTabCompleter(new TemplateTabCompleter());

        getServer().getConsoleSender().sendMessage(changeColor("&aEpicPunishments is now enabled"));
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(changeColor("&cEpicPunishments is now disabled"));
    }
}
