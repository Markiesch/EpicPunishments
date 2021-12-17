package com.markiesch.listeners;

import com.markiesch.EpicPunishments;
import com.markiesch.utils.PlayerStorage;
import com.markiesch.utils.TimeUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class ChatListener implements Listener {
    EpicPunishments plugin = EpicPunishments.getInstance();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        boolean isMuted = PlayerStorage.isMuted(player.getUniqueId());
        if (!isMuted) return;

        event.setCancelled(true);
        String mute = getMute(PlayerStorage.getPunishments(player.getUniqueId()));
        String reason = mute.split(";")[2];
        long duration = Long.parseLong(mute.split(";")[4]) - System.currentTimeMillis();

        String message = plugin.getConfig().getString("messages." + ( duration == 0L ? "permanentlyMute" : "temporarilyMute"));
        if (message == null) return;
        message = message.replace("[duration]", TimeUtils.makeReadable(duration)).replace("[reason]", reason);
        player.sendMessage(message);
    }

    public static String getMute(List<String> infractions) {
        long highestDuration = 0L;
        String punishment = null;

        for (String infraction : infractions) {
            String type = infraction.split(";")[1];
            if (!"mute".equalsIgnoreCase(type)) continue;
            long duration = Long.parseLong(infraction.split(";")[3]);
            if (Long.parseLong(infraction.split(";")[4]) - System.currentTimeMillis() < 0) continue;
            if (duration == 0L) return infraction;
            if (duration > highestDuration) {
                punishment = infraction;
                highestDuration = duration;
            }
        }

        return punishment;
    }
}
