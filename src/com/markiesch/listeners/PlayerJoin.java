package com.markiesch.listeners;

import com.markiesch.EpicPunishments;
import com.markiesch.utils.InputUtils;
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
        plugin.getPlayerStorage().createPlayerProfile(player.getUniqueId());

        if (!plugin.getPlayerStorage().isPlayerBanned(player.getUniqueId())) return;

        List<String> infractions = plugin.getPlayerStorage().getConfig().getStringList(player.getUniqueId() + ".infractions");
        long duration = getBanDuration(infractions);
        String reason = getReasonByDuration(infractions, duration);
        if (duration == 0L) {
            player.kickPlayer("§cYou are permanently banned from this server!\n\n§7Reason: §f" + reason + "\n§7Find out more: §e§nwww.example.com");
        } else {
            player.kickPlayer("§cYou are temporarily banned for §f" + TimeUtils.makeReadable(duration) + " §cfrom this server!\n\n§7Reason: §f" + reason + "\n§7Find out more: §e§nwww.example.com");
        }
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
