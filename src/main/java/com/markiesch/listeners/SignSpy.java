package com.markiesch.listeners;

import com.markiesch.locale.Translation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignSpy implements Listener {
    @EventHandler
    public boolean onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("epicpunishments.spy.sign.bypass")) return true;

        player.sendMessage(Translation.EVENT_SIGN_SPY
                .addPlaceholder("target", player.getName())
                .addPlaceholder("content", String.join(", ", event.getLines()))
                .toString());

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.equals(player)) continue;
            if (onlinePlayer.hasPermission("epicpunishments.spy.sign")) {
                player.sendMessage(Translation.EVENT_SIGN_SPY
                        .addPlaceholder("target", player.getName())
                        .addPlaceholder("content", String.join(" ", event.getLines()))
                        .toString()
                );
            }
        }
        return true;
    }
}
