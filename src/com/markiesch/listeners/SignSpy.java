package com.markiesch.listeners;

import com.markiesch.EpicPunishments;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignSpy implements Listener {
    EpicPunishments plugin = EpicPunishments.getInstance();
    @EventHandler
    public boolean onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("epicpunishments.spy.sign.bypass")) return true;
        String lines = event.getLine(0) + " " + event.getLine(1) + " " + event.getLine(2) + " " + event.getLine(3);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.equals(player)) break;
            if (onlinePlayer.hasPermission("bangui.spy.sign")) {
                String config = plugin.getConfig().getString("messages.signspy");
                if (config == null) return true;
                String message = config.replace("[target]", player.getName()).replace("[content]", "" + lines);
                onlinePlayer.sendMessage(plugin.changeColor(message));
            }
        }
        return true;
    }
}
