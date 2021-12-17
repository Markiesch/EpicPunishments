package com.markiesch.listeners;

import com.markiesch.EpicPunishments;
import com.markiesch.utils.PlayerStorage;
import com.markiesch.utils.TimeUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.UUID;

public class PlayerJoin implements Listener {
    EpicPunishments plugin = EpicPunishments.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerStorage.createPlayerProfile(player.getUniqueId());
        if (!PlayerStorage.isPlayerBanned(player.getUniqueId())) return;

        String[] ban = getBan(PlayerStorage.getPunishments(player.getUniqueId())).split(";");
        String reason = ban[2];
        String duration = Long.parseLong(ban[3]) == 0L ? "Permanent" : TimeUtils.makeReadable(Long.parseLong(ban[4]) - System.currentTimeMillis());
        String message = plugin.getConfig().getString("messages." + (duration.equals("Permanent") ? "permBanMessage" : "tempBanMessage"));
        if (message != null) message = message.replace("[reason]", reason).replace("[duration]", duration);
        player.kickPlayer(message);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!plugin.getEditor().containsKey(uuid)) return;
        plugin.getEditor().get(uuid).cancel();
    }

    public static String getBan(List<String> infractions) {
        long currentTime = System.currentTimeMillis();
        long highestDuration = 0L;
        String punishment = null;

        for (String infraction : infractions) {
            String type = infraction.split(";")[1];
            if (!"ban".equalsIgnoreCase(type)) continue;
            long duration = Long.parseLong(infraction.split(";")[3]);
            if (duration == 0L) return infraction;
            if (Long.parseLong(infraction.split(";")[4]) - currentTime < 0) continue;

            if (duration > highestDuration) {
                punishment = infraction;
                highestDuration = duration;
            }
        }
        return punishment;
    }
}
