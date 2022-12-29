package com.markiesch;

import com.markiesch.commands.*;
import com.markiesch.listeners.*;
import com.markiesch.locale.LangConfig;
import com.markiesch.modules.infraction.InfractionManager;
import com.markiesch.modules.profile.ProfileManager;
import com.markiesch.storage.Storage;
import org.bstats.bukkit.Metrics;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class EpicPunishments extends JavaPlugin implements Listener {
    private static final int pluginId = 17132;

    private static LangConfig langConfig;

    public static LangConfig getLangConfig() {
        return langConfig;
    }

    private static EpicPunishments instance;

    public static EpicPunishments getInstance() {
        return instance;
    }

    public void onEnable() {
        new Metrics(this, pluginId);

        // Initialize config
        instance = this;
        this.saveDefaultConfig();
        langConfig = new LangConfig(this);

        // Initialize storage
        Storage.getInstance().setup(this);
        InfractionManager.getInstance().initialize();
        ProfileManager.getInstance().initialize();

        registerListeners();
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

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);

        final boolean commandSpyIsEnabled = getConfig().getBoolean("modules.command_spy");
        final boolean signSpyIsEnabled = getConfig().getBoolean("modules.sign_spy");

        if (commandSpyIsEnabled) getServer().getPluginManager().registerEvents(new CommandSpy(), this);
        if (signSpyIsEnabled) getServer().getPluginManager().registerEvents(new SignSpy(), this);
    }
}
