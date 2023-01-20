package com.markiesch.modules.infraction;

import com.markiesch.Permission;
import com.markiesch.event.PunishEvent;
import com.markiesch.locale.Translation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.stream.Collectors;

public class InfractionBroadcaster implements Listener {
    @EventHandler
    public void broadcastListener(PunishEvent event) {
        InfractionModel infractionModel = event.getInfraction();

        List<Player> players = Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> player.hasPermission(Permission.BROADCAST_RECEIVE.getNode()))
                .collect(Collectors.toList());

        for (Player player : players) {
            Translation translation = infractionModel.isPermanent() ? Translation.BROADCAST_PERMANENTLY : Translation.BROADCAST_TEMPORARILY;

            player.sendMessage(translation
                    .addPlaceholder("victim", Bukkit.getOfflinePlayer(infractionModel.victim).getName())
                    .addPlaceholder("type", infractionModel.type.getTranslation())
                    .addPlaceholder("issuer", infractionModel.getIssuer())
                    .addPlaceholder("duration", infractionModel.getFormattedDuration())
                    .toString()
            );
        }
    }
}
