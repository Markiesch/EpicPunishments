package com.markiesch;

import com.markiesch.commands.*;
import com.markiesch.listeners.*;
import com.markiesch.locale.LangConfig;
import com.markiesch.modules.infraction.InfractionManager;
import com.markiesch.modules.profile.ProfileManager;
import com.markiesch.storage.Storage;
import com.zaxxer.hikari.pool.HikariPool;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class EpicPunishments extends JavaPlugin implements Listener {
    private static final int BSTATS_PLUGIN_ID = 17132;

    private static LangConfig langConfig;
    private static EpicPunishments instance;

    public static LangConfig getLangConfig() {
        return langConfig;
    }

    public static EpicPunishments getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;

        // Initialize config
        this.saveDefaultConfig();
        langConfig = new LangConfig(this);

        // Initialize storage
        try {
            Storage.getInstance().init(this);
            Storage.getInstance().createDatabaseTables(this);
        } catch (SQLException | HikariPool.PoolInitializationException e) {
            Bukkit.getPluginManager().disablePlugin(this);
            getLogger().warning("Failed to connect with database, please check your credentials!");
            return;
        }
        InfractionManager.getInstance().initialize();
        ProfileManager.getInstance().initialize();

        registerListeners();
        registerCommands();

        new Metrics(this, BSTATS_PLUGIN_ID);
        getServer().getConsoleSender().sendMessage("§aEpicPunishments is now enabled");
    }

    public void onDisable() {
        Storage.getInstance().closeConnection();

        getServer().getConsoleSender().sendMessage("§cEpicPunishments is now disabled");
    }

    private void registerCommands() {
        new PunishCommand(this);
        new TemplatesCommand(this);
        new RandomPlayerCommand();
        new ClearChatCommand();

        new BanCommand();
        new UnbanCommand();
        new MuteCommand();
        new UnmuteCommand();
        new KickCommand();
        new ImMutedCommand(this);
        new HistoryCommand(this);
        new InfoCommand();
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
