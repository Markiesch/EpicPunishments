package com.markiesch.listeners;

import com.markiesch.locale.Translation;
import com.markiesch.modules.infraction.InfractionList;
import com.markiesch.modules.infraction.InfractionManager;
import com.markiesch.modules.infraction.InfractionModel;
import com.markiesch.modules.infraction.InfractionType;
import com.markiesch.utils.TimeUtils;
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

        InfractionModel infraction = InfractionManager.getInstance().getPlayer(player.getUniqueId()).getActiveByType(InfractionType.MUTE).get(0);

        Translation translation = infraction.isPermanent() ? Translation.EVENT_MUTE_PERMANENTLY_MESSAGE : Translation.EVENT_MUTE_TEMPORARILY_MESSAGE;

        String message = translation
                .addPlaceholder("duration", TimeUtils.makeReadable(infraction.duration))
                .addPlaceholder("reason", infraction.reason)
                .toString();

        player.sendMessage(message);
    }
}
