package com.markiesch.listeners;

import com.markiesch.EpicPunishments;
import com.markiesch.modules.profile.ProfileController;
import com.markiesch.modules.infraction.InfractionModel;
import com.markiesch.modules.profile.ProfileModel;
import com.markiesch.utils.InfractionType;
import com.markiesch.utils.TimeUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private final EpicPunishments plugin;

    public ChatListener(EpicPunishments plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        ProfileModel profile = new ProfileController().getProfile(player.getUniqueId());

        if (!profile.isMuted()) return;

        event.setCancelled(true);

        InfractionModel infraction = profile.getActiveInfractions(InfractionType.MUTE).get(0);

        String message = (
        infraction.isPermanent() ?
            "§c———————————————————————————\n§c§lHey! §cYou are still muted for [duration]!\n§7Reason: §c[reason]\n§7Find out more here:§c www.example.com/faq#muted\n§c———————————————————————————"  :
            "§c———————————————————————————\n§c§lHey! §cYou are currently muted!\n§7Reason: §c[reason]\n§7Find out more here:§c www.example.com/faq#muted\n§c———————————————————————————")
                .replace("[duration]", TimeUtils.makeReadable(infraction.duration))
                .replace("[reason]", infraction.reason);


        player.sendMessage(message);
    }
}
