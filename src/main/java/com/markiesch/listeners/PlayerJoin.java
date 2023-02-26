package com.markiesch.listeners;

import com.markiesch.EpicPunishments;
import com.markiesch.Format;
import com.markiesch.Permission;
import com.markiesch.locale.Translation;
import com.markiesch.modules.infraction.InfractionList;
import com.markiesch.modules.infraction.InfractionManager;
import com.markiesch.modules.infraction.InfractionModel;
import com.markiesch.modules.infraction.InfractionType;
import com.markiesch.modules.profile.ProfileManager;
import com.markiesch.modules.profile.ProfileModel;
import com.markiesch.modules.warning.WarningManager;
import com.markiesch.modules.warning.WarningModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerJoin implements Listener {
    private final EpicPunishments plugin;

    public PlayerJoin(EpicPunishments plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        notifyStaffWithNewName(event);

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

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            Player player = event.getPlayer();
            if (!player.isOnline()) return;

            List<WarningModel> warnings = WarningManager.getInstance()
                    .getPlayer(player.getUniqueId())
                    .stream()
                    .filter(warningModel -> !warningModel.hasSeenMessage())
                    .collect(Collectors.toList());

            for (WarningModel warning : warnings) {
                player.sendMessage(warning.getFormat());
                WarningManager.getInstance().updateWarning(warning.getId(), true);
            }
        }, 60L);
    }

    private void notifyStaffWithNewName(AsyncPlayerPreLoginEvent event) {
        ProfileModel currentProfile = ProfileManager.getInstance().getPlayer(event.getUniqueId());

        if (currentProfile == null || event.getName().equals(currentProfile.getName())) return;

        Bukkit.getOnlinePlayers().stream()
                .filter(onlinePlayer -> onlinePlayer.hasPermission(Permission.SPY_NEW_NAME_NOTIFY.getNode()))
                .forEach(onlinePlayer -> {
                    onlinePlayer.sendMessage(Translation.EVENT_NEW_NAME_SPY
                            .addPlaceholder("old_name", currentProfile.getName())
                            .addPlaceholder("new_name", event.getName())
                            .toString()
                    );
                });
    }
}
