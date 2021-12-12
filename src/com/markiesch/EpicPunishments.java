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
import java.util.UUID;

public class EpicPunishments extends JavaPlugin implements Listener {
    private static EpicPunishments instance;
    public static EpicPunishments getInstance() {
        return instance;
    }

    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    private final Map<UUID, InputUtils> editor = new HashMap<>();

    public Map<UUID, InputUtils> getEditor() {
        return this.editor;
    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player player) {
        PlayerMenuUtility playerMenuUtility;

        if (playerMenuUtilityMap.containsKey(player)) {
            return playerMenuUtilityMap.get(player);
        } else {
            playerMenuUtility = new PlayerMenuUtility(player);
            playerMenuUtilityMap.put(player, playerMenuUtility);
            return playerMenuUtility;
        }
    }

    public String changeColor(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }


    @Override
    public void onEnable() {
        instance = this;
        PlayerStorage.saveDefaultConfig();
        TemplateStorage.saveDefaultConfig();
        this.saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new CommandSpy(), this);
        getServer().getPluginManager().registerEvents(new SignSpy(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInput(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);

        new KickCommand();
        new MuteCommand();
        new UnmuteCommand();
        new BanCommand();
        new UnbanCommand();
        new PunishCommand();
        new TemplatesCommand();

        getServer().getConsoleSender().sendMessage(changeColor("&aEpicPunishments is now enabled"));
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(changeColor("&cEpicPunishments is now disabled"));
    }
}
