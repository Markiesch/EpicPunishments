package com.markiesch;

import com.markiesch.controllers.InfractionController;
import com.markiesch.controllers.ProfileController;
import com.markiesch.commands.*;
import com.markiesch.listeners.*;
import com.markiesch.storage.SQL.SQLite;
import com.markiesch.storage.Storage;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class EpicPunishments extends JavaPlugin implements Listener {
    private Storage storage;
    public Storage getStorage() {
        return storage;
    }

    private ProfileController profileController;
    public ProfileController getProfileController() {
        return profileController;
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

        // Initialize controllers
        profileController = new ProfileController(this);
        infractionController = new InfractionController(this);

        // Initialize storage
        storage = new SQLite(this);

        // Initialize listeners
        getServer().getPluginManager().registerEvents(new CommandSpy(), this);
        getServer().getPluginManager().registerEvents(new SignSpy(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);

        // Initialize commands
        new BanCommand(this);
        new UnBanCommand();

        getServer().getConsoleSender().sendMessage("§aEpicPunishments is now enabled");
    }

    public void onDisable() {
        if (storage instanceof SQLite) {
            SQLite sqLite = (SQLite) storage;
            sqLite.closeConnection();
        }
        
        getServer().getConsoleSender().sendMessage("§cEpicPunishments is now disabled");
    }
}
