package com.markiesch.listeners;

import com.markiesch.EpicPunishments;
import com.markiesch.Format;
import com.markiesch.modules.infraction.InfractionManager;
import com.markiesch.modules.infraction.InfractionModel;
import com.markiesch.modules.infraction.InfractionType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;
import java.util.Locale;

public class MuteCommandListener implements Listener {
    private final EpicPunishments plugin;

    public MuteCommandListener(EpicPunishments plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void muteCommandListener(PlayerCommandPreprocessEvent event) {
        List<InfractionModel> activeMutes = InfractionManager.getInstance()
            .getPlayer(event.getPlayer().getUniqueId())
            .getActiveByType(InfractionType.MUTE);

        if (activeMutes.isEmpty()) return;

        if (isBlacklistedMuteCommand(event.getMessage())) {
            InfractionModel activeMute = activeMutes.get(0);

            String message = activeMute.isPermanent() ?
                Format.PERMANENTLY_MUTED.getString(activeMute) :
                Format.TEMPORARILY_MUTED.getString(activeMute);
            event.getPlayer().sendMessage(message);
            event.setCancelled(true);
        }
    }

    private boolean isBlacklistedMuteCommand(String command) {
        String[] splitString = command.split(":", 2);
        String commandWithoutPrefix = splitString[0].contains(" ") || splitString.length == 1 ? command : "/" + splitString[1];

        String parsedString = (commandWithoutPrefix + " ").toLowerCase(Locale.ROOT);

        return plugin.getConfig().getStringList("mutes.command_blacklist").stream()
            .map((blacklistedCommand) -> "/" + blacklistedCommand.toLowerCase(Locale.ROOT) + " ")
            .anyMatch(parsedString::startsWith);
    }
}
