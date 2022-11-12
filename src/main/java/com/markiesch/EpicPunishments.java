package com.markiesch;

import com.markiesch.commands.*;
import com.markiesch.listeners.*;
import com.markiesch.storage.Storage;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class EpicPunishments extends JavaPlugin implements Listener {
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
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);

        // Initialize commands
        registerCommands();

        getServer().getConsoleSender().sendMessage("§aEpicPunishments is now enabled");
    }

    private void registerCommands() {
        new BanCommand();
        new KickCommand();
        new MuteCommand();
        new PunishCommand(this);
        new TemplatesCommand(this);
    }

    public void onDisable() {
        Storage.getInstance().closeConnection();
        
        getServer().getConsoleSender().sendMessage("§cEpicPunishments is now disabled");
    }
}
