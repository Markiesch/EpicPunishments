package com.markiesch.listeners;

import com.markiesch.Permission;
import com.markiesch.locale.Translation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignSpy implements Listener {
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission(Permission.SPY_SIGN_BYPASS.getNode())) return;

        Bukkit.getOnlinePlayers()
                .stream()
                .filter(onlinePlayer -> onlinePlayer.hasPermission(Permission.SPY_SIGN_NOTIFY.getNode()))
                .forEach(staffMember -> {
                    staffMember.sendMessage(Translation.EVENT_SIGN_SPY
                            .addPlaceholder("target", player.getName())
                            .addPlaceholder("content", String.join(", ", event.getLines()))
                            .toString());
                });
    }
}
