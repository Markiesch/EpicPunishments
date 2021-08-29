package com.markiesch.listeners;

import com.markiesch.EpicPunishments;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private final EpicPunishments plugin = EpicPunishments.getInstance();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        boolean isMuted = plugin.getPlayerStorage().isMuted(player.getUniqueId());
        if (!isMuted) return;

        e.setCancelled(true);
        player.sendMessage("§c———————————————————————————\n§c§lHey! §cYou are currently muted!\n§7Find out more here: §cwww.example.com/faq#muted\n§c———————————————————————————");
    }
}
