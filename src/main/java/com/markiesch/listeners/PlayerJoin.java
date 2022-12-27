package com.markiesch.listeners;

import com.markiesch.Format;
import com.markiesch.modules.infraction.InfractionList;
import com.markiesch.modules.infraction.InfractionManager;
import com.markiesch.modules.infraction.InfractionModel;
import com.markiesch.modules.infraction.InfractionType;
import com.markiesch.modules.profile.ProfileManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerJoin implements Listener {
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

        event.disallow(
                AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
                activeBan.isPermanent() ?
                        Format.PERMANENTLY_BANNED.getString(activeBan) :
                        Format.TEMPORARILY_BANNED.getString(activeBan)
        );
    }
}
