package com.markiesch;

import com.markiesch.commands.*;
import com.markiesch.listeners.*;
import com.markiesch.locale.LangConfig;
import com.markiesch.modules.infraction.InfractionManager;
import com.markiesch.modules.profile.ProfileManager;
import com.markiesch.storage.Storage;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class EpicPunishments extends JavaPlugin implements Listener {
    private static LangConfig langConfig;
    public static LangConfig getLangConfig() {
        return langConfig;
    }

    public String changeColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public void onEnable() {
        // Initialize config
        this.saveDefaultConfig();
        langConfig = new LangConfig(this);

        // Initialize storage
        Storage.getInstance().setup(this);
        InfractionManager.getInstance().initialize();
        ProfileManager.getInstance().initialize();

        // Initialize listeners
        getServer().getPluginManager().registerEvents(new CommandSpy(), this);
        getServer().getPluginManager().registerEvents(new SignSpy(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);

        // Initialize commands
        registerCommands();

        getServer().getConsoleSender().sendMessage("§aEpicPunishments is now enabled");
    }

    public void onDisable() {
        Storage.getInstance().closeConnection();

        getServer().getConsoleSender().sendMessage("§cEpicPunishments is now disabled");
    }

    private void registerCommands() {
        new BanCommand();
        new KickCommand();
        new MuteCommand();

        new UnbanCommand();
        new UnmuteCommand();

        new PunishCommand(this);
        new TemplatesCommand(this);
    }
}
