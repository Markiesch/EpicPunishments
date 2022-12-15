package com.markiesch.listeners;

import com.markiesch.Format;
import com.markiesch.modules.infraction.InfractionList;
import com.markiesch.modules.infraction.InfractionManager;
import com.markiesch.modules.infraction.InfractionModel;
import com.markiesch.modules.infraction.InfractionType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        InfractionList infractionList = InfractionManager.getInstance().getPlayer(player.getUniqueId());

        if (!infractionList.isMuted()) return;

        event.setCancelled(true);

        InfractionModel activeMute = InfractionManager.getInstance()
                .getPlayer(player.getUniqueId())
                .getActiveByType(InfractionType.MUTE)
                .get(0);

        String message = activeMute.isPermanent() ?
                Format.PERMANENTLY_MUTED.getString(activeMute) :
                Format.TEMPORARILY_MUTED.getString(activeMute);
        player.sendMessage(message);
    }
}
