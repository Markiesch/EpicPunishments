package com.markiesch.listeners;

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

        String message = (
        infraction.isPermanent() ?
            "§c———————————————————————————\n§c§lHey! §cYou are currently muted!\n§7Reason: §c[reason]\n§7Find out more here:§c www.example.com/faq#muted\n§c———————————————————————————" :
            "§c———————————————————————————\n§c§lHey! §cYou are still muted for [duration]!\n§7Reason: §c[reason]\n§7Find out more here:§c www.example.com/faq#muted\n§c———————————————————————————")
                .replace("[duration]", TimeUtils.makeReadable(infraction.duration))
                .replace("[reason]", infraction.reason);


        player.sendMessage(message);
    }
}
