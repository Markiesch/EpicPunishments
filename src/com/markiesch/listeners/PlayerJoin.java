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

        List<String> infractions = PlayerStorage.getConfig().getStringList(player.getUniqueId() + ".infractions");
        long duration = getBanDuration(infractions);
        String reason = getReasonByDuration(infractions, duration);
        String message = plugin.getConfig().getString("messages." + (duration == 0L ? "permBanMessage" : "tempBanMessage"));
        if (message != null) message = message.replace("[reason]", reason).replace("[duration]", TimeUtils.makeReadable(duration) + "");
        player.kickPlayer(message);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!plugin.getEditor().containsKey(uuid)) return;
        plugin.getEditor().get(uuid).cancel();
    }

    public static long getBanDuration(List<String> infractions) {
        long highestDuration = 0L;

        for (String infraction : infractions) {
            String type = infraction.split(";")[1];
            if (!"ban".equalsIgnoreCase(type)) continue;
            long currentTime = System.currentTimeMillis();
            long duration = Long.parseLong(infraction.split(";")[4]) - currentTime;
            if (duration > highestDuration) highestDuration = duration;
        }
        return highestDuration;
    }

    public static String getReasonByDuration(List<String> infractions, long duration) {
        String reason = "none";
        long highestDuration = 0L;

        for (String infraction : infractions) {
            String type = infraction.split(";")[1];
            if (!"ban".equalsIgnoreCase(type)) continue;
            long currentTime = System.currentTimeMillis();
            long expires = Long.parseLong(infraction.split(";")[4]) - currentTime;
            String configReason = infraction.split(";")[2];
            if (expires < 0) return configReason;
            if (expires > highestDuration && expires == duration) reason = configReason;
        }
        return reason;
    }
}
