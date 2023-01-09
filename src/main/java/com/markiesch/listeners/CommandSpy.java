package com.markiesch.listeners;

import com.markiesch.Permission;
import com.markiesch.locale.Translation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandSpy implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPreCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission(Permission.SPY_COMMAND_BYPASS.getNode())) return;

        Bukkit.getOnlinePlayers()
                .stream()
                .filter(onlinePlayer -> onlinePlayer.hasPermission(Permission.SPY_COMMAND_NOTIFY.getNode()))
                .forEach(onlineStaff -> {
                    onlineStaff.sendMessage(Translation.EVENT_COMMAND_SPY
                            .addPlaceholder("target", player.getName())
                            .addPlaceholder("command", event.getMessage())
                            .toString());
                });
    }
}
