package com.markiesch.listeners;

import com.markiesch.EpicPunishments;
import com.markiesch.locale.Translation;
import com.markiesch.modules.infraction.InfractionList;
import com.markiesch.modules.infraction.InfractionManager;
import com.markiesch.modules.infraction.InfractionModel;
import com.markiesch.modules.infraction.InfractionType;
import com.markiesch.modules.profile.ProfileManager;
import com.markiesch.utils.SkullTexture;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    private final EpicPunishments plugin;

    public PlayerJoin(EpicPunishments plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String skinTextureURL = Bukkit.getOnlineMode() ? SkullTexture.fromPlayerUUID(event.getPlayer()) : SkullTexture.fromMojangAPI(event.getPlayer().getName());

            ProfileManager.getInstance().updateSkullTexture(event.getPlayer().getUniqueId(), skinTextureURL);
        });
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        boolean success = ProfileManager.getInstance()
                .handlePlayerLogin(
                        event.getUniqueId(),
                        event.getName(),
                        event.getAddress().getHostAddress()
                );

        if (!success) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Your profile couldn't be created or found, please rejoin");
            return;
        }

        InfractionList infractions = InfractionManager.getInstance().getPlayer(event.getUniqueId());
        InfractionModel activeBan = infractions.getActiveByType(InfractionType.BAN)
                .stream()
                .findFirst()
                .orElse(null);

        if (activeBan == null) return;

        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
                (activeBan.isPermanent() ?
                        Translation.EVENT_BAN_PERMANENTLY_MESSAGE :
                        Translation.EVENT_BAN_TEMPORARILY_MESSAGE)
                        .addPlaceholder("reason", activeBan.reason)
                        .addPlaceholder("duration", TimeUtils.makeReadable(activeBan.duration))
                        .toString()
        );
    }
}
