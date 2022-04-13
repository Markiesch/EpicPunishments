package com.markiesch;

import com.markiesch.controllers.InfractionController;
import com.markiesch.controllers.ProfileController;
import com.markiesch.commands.*;
import com.markiesch.listeners.*;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.storage.Storage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EpicPunishments extends JavaPlugin implements Listener {
    private Storage storage;
    public Storage getStorage() {
        return storage;
    }

    private ProfileController profileController;
    public ProfileController getProfileController() {
        return profileController;
    }

    public final ConcurrentHashMap<UUID, PlayerMenuUtility> playerMenuUtilityMap = new ConcurrentHashMap<>();
    public PlayerMenuUtility getPlayerMenuUtility(UUID uuid) {
        if (playerMenuUtilityMap.containsKey(uuid)) return playerMenuUtilityMap.get(uuid);

        PlayerMenuUtility playerMenuUtility = new PlayerMenuUtility(uuid);
        playerMenuUtilityMap.put(uuid, playerMenuUtility);
        return getPlayerMenuUtility(uuid);
    }

    public void removePlayerMenuUtility(UUID uuid) {

    }

    private InfractionController infractionController;
    public InfractionController getInfractionController() {
        return infractionController;
    }

    public String changeColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public void onEnable() {
        // Initialize config
        this.saveDefaultConfig();

        // Initialize storage
        storage = new Storage(this);

        // Initialize controllers
        profileController = new ProfileController(this);
        infractionController = new InfractionController(this);

        // Initialize listeners
        getServer().getPluginManager().registerEvents(new CommandSpy(), this);
        getServer().getPluginManager().registerEvents(new SignSpy(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);

        // Initialize commands
        new BanCommand(this);
        new UnBanCommand();
        new PunishCommand(this);

        getServer().getConsoleSender().sendMessage("§aEpicPunishments is now enabled");
    }

    public void onDisable() {
        storage.closeConnection();
        
        getServer().getConsoleSender().sendMessage("§cEpicPunishments is now disabled");
    }
}
