package com.markiesch.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandSpy implements Listener {
    @EventHandler
    public boolean onPreCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("epicpunishments.spy.command.bypass")) return true;

        String message = event.getMessage();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.equals(player) || !onlinePlayer.hasPermission("epicpunishments.spy.command")) continue;
            onlinePlayer.sendMessage("§cCSpy §7" + player.getDisplayName() + ": " + message);
        }
        return true;
    }
}
