package com.markiesch.listeners;

import com.markiesch.EpicPunishments;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandSpy implements Listener {
    EpicPunishments plugin = EpicPunishments.getInstance();

    @EventHandler
    public boolean onPreCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("epicpunishments.spy.command.bypass")) return true;

        String message = event.getMessage();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.equals(player)) break;
            if (onlinePlayer.hasPermission("epicpunishments.spy.command")) {
                onlinePlayer.sendMessage(plugin.changeColor("&cCSpy &7" + player.getDisplayName() + ": " + message));
            }
        }
        return true;
    }
}
