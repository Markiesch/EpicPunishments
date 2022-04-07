package com.markiesch;

import com.markiesch.controllers.TemplateController;
import com.markiesch.commands.*;
import com.markiesch.listeners.*;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.utils.InputUtils;
import com.markiesch.utils.PlayerStorage;
import com.markiesch.utils.configs.LangConfig;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EpicPunishments extends JavaPlugin implements Listener {
    private static TemplateController templateController;
    public static TemplateController getTemplateController() {
        return templateController;
    }

    private static final ConcurrentHashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<UUID, InputUtils> editor = new ConcurrentHashMap<>();
    public ConcurrentHashMap<UUID, InputUtils> getEditor() { return this.editor; }

    private static EpicPunishments instance;
    public static EpicPunishments getInstance() { return instance; }

    private static LangConfig langConfig;
    public static LangConfig getLangConfig() { return langConfig; };

    public static PlayerMenuUtility getPlayerMenuUtility(Player player) {
        PlayerMenuUtility playerMenuUtility;

        if (playerMenuUtilityMap.containsKey(player)) return playerMenuUtilityMap.get(player);

        playerMenuUtility = new PlayerMenuUtility(player);
        playerMenuUtilityMap.put(player, playerMenuUtility);
        return playerMenuUtility;
    }

    public String changeColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public void onEnable() {
        instance = this;

        templateController = new TemplateController();


        PlayerStorage.saveDefaultConfig();
        langConfig = new LangConfig(this);
        this.saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new CommandSpy(), this);
        getServer().getPluginManager().registerEvents(new SignSpy(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInput(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);

        new KickCommand();
        new WarnCommand();
        new MuteCommand();
        new UnMuteCommand();
        new BanCommand();
        new UnBanCommand();
        new PunishCommand();
        new TemplatesCommand();

        getServer().getConsoleSender().sendMessage("§aEpicPunishments is now enabled");
    }

    public void onDisable() {
        getServer().getConsoleSender().sendMessage("§cEpicPunishments is now disabled");
    }
}
