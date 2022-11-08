package com.markiesch;

import com.markiesch.commands.*;
import com.markiesch.listeners.*;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.storage.Storage;
import com.markiesch.utils.InputUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EpicPunishments extends JavaPlugin implements Listener {
    public final ConcurrentHashMap<UUID, PlayerMenuUtility> playerMenuUtilityMap = new ConcurrentHashMap<>();
    public PlayerMenuUtility getPlayerMenuUtility(UUID uuid) {
        if (playerMenuUtilityMap.containsKey(uuid)) return playerMenuUtilityMap.get(uuid);

        PlayerMenuUtility playerMenuUtility = new PlayerMenuUtility(uuid);
        playerMenuUtilityMap.put(uuid, playerMenuUtility);
        return getPlayerMenuUtility(uuid);
    }

    private final ConcurrentHashMap<UUID, InputUtils> editor = new ConcurrentHashMap<>();
    public ConcurrentHashMap<UUID, InputUtils> getEditor() { return this.editor; }

    public String changeColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public void onEnable() {
        // Initialize config
        this.saveDefaultConfig();

        // Initialize storage
        Storage.getInstance().setup(this);

        // Initialize listeners
        getServer().getPluginManager().registerEvents(new CommandSpy(), this);
        getServer().getPluginManager().registerEvents(new SignSpy(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInput(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        // Initialize commands
        new BanCommand();
        new MuteCommand();
        new WarnCommand();
        new PunishCommand(this);
        new KickCommand();

        getServer().getConsoleSender().sendMessage("§aEpicPunishments is now enabled");
    }

    public void onDisable() {
        Storage.getInstance().closeConnection();
        
        getServer().getConsoleSender().sendMessage("§cEpicPunishments is now disabled");
    }
}
