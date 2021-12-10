package com.markiesch.listeners;

import com.markiesch.EpicPunishments;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    EpicPunishments plugin = EpicPunishments.getInstance();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        boolean isMuted = plugin.getPlayerStorage().isMuted(player.getUniqueId());
        if (!isMuted) return;

        event.setCancelled(true);
        String message = plugin.getConfig().getString("messages.temporarilyMute");
        if (message == null) return;
        player.sendMessage(message);
    }
}
