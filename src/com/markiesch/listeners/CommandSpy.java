package com.markiesch.listeners;

import com.markiesch.EpicPunishments;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandSpy implements Listener {
    private static final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);

    @EventHandler
    public boolean onPreCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("bangui.spy.command.bypass")) return true;

        String message = event.getMessage();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer == player) break;
            if (onlinePlayer.hasPermission("bangui.spy.command")) {
                onlinePlayer.sendMessage(plugin.changeColor("&cCSpy &7" + player.getDisplayName() + ": " + message));
            }
        }
        return true;
    }
}
