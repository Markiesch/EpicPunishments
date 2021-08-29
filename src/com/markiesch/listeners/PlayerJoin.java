package com.markiesch.listeners;

import com.markiesch.EpicPunishments;
import com.markiesch.utils.TimeUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class PlayerJoin implements Listener {
    private final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        plugin.getPlayerStorage().createPlayerProfile(e.getPlayer().getUniqueId());
        Player player = e.getPlayer();

        if (plugin.getPlayerStorage().isPlayerBanned(player.getUniqueId())) {
            List<String> infractions = plugin.getPlayerStorage().getConfig().getStringList(player.getUniqueId() + ".infractions");
            long duration = getBanDuration(infractions);
            String reason = getReasonByDuration(infractions, duration);
            if (duration == 0L) {
                player.kickPlayer("§cYou are permanently banned from this server!\n\n§7Reason: §f" + reason + "\n§7Find out more: §e§nwww.example.com");
            } else {
                player.kickPlayer("§cYou are temporarily banned for §f" + TimeUtils.makeReadable(duration) + " §cfrom this server!\n\n§7Reason: §f" + reason + "\n§7Find out more: §e§nwww.example.com");
            }
        }
    }

    public static long getBanDuration(List<String> infractions) {
        long highestDuration = 0L;

        for (String infraction : infractions) {
            String type = infraction.split(";")[1];
            if (!type.equalsIgnoreCase("ban")) continue;
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
            if (!type.equalsIgnoreCase("ban")) continue;
            long currentTime = System.currentTimeMillis();
            long expires = Long.parseLong(infraction.split(";")[4]) - currentTime;
            String configReason = infraction.split(";")[2];
            if (expires < 0) return configReason;
            if (expires > highestDuration && expires == duration) reason = configReason;
        }
        return reason;
    }
}
