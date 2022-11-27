package com.markiesch.modules.infraction;

import com.markiesch.locale.Translation;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PreparedInfraction {
    public final InfractionType type;
    public final CommandSender issuer;
    public final OfflinePlayer victim;
    public final String reason;
    public final long duration;
    public final long date;

    public PreparedInfraction(InfractionType type, CommandSender issuer, OfflinePlayer victim, String reason, long duration) {
        this.type = type;
        this.issuer = issuer;
        this.victim = victim;
        this.reason = reason;
        this.duration = duration;
        date = System.currentTimeMillis() / 1000L;
    }

    public void execute() {
        InfractionList victimInfractionList = InfractionManager.getInstance().getPlayer(victim.getUniqueId());

        switch (type) {
            case KICK -> {
                if (!victim.isOnline()) {
                    issuer.sendMessage(Translation.EVENT_KICK_OFFLINE.addPlaceholder("victim_name", victim.getName()).toString());
                    return;
                }
                Player onlineTarget = victim.getPlayer();
                if (onlineTarget != null) {
                    onlineTarget.kickPlayer(Translation.EVENT_KICK_MESSAGE.addPlaceholder("message", reason).toString());

                    issuer.sendMessage(Translation.EVENT_KICK_SUCCESS.addPlaceholder("victim_name", victim.getName()).toString());
                }
            }
            case MUTE -> {
                if (victimInfractionList.isMuted()) {
                    issuer.sendMessage(
                            Translation.EVENT_MUTE_ALREADY
                                    .addPlaceholder("victim_name", victim.getName())
                                    .toString());
                    return;
                }
                issuer.sendMessage(Translation.EVENT_MUTE_SUCCESS.addPlaceholder("victim_name", victim.getName()).toString());
            }
            case BAN -> {
                if (victimInfractionList.isBanned()) {
                    issuer.sendMessage(Translation.EVENT_BAN_ALREADY.addPlaceholder("victim_name", victim.getName()).toString());
                    return;
                }

                if (victim.isOnline()) {
                    Player onlineVictim = victim.getPlayer();
                    if (onlineVictim != null) {
                        onlineVictim.kickPlayer("You have been banned: \nReason:" + reason);
                    }

                    issuer.sendMessage(Translation.EVENT_BAN_SUCCESS.addPlaceholder("victim_name", victim.getName()).toString());
                }
            }
        }

        InfractionManager.getInstance().createInfraction(this);
    }

    private @Nullable UUID getIssuerUUID() {
        return issuer instanceof OfflinePlayer ? ((Player)issuer).getUniqueId() : null;
    }

    public InfractionModel createInfraction(int id) {
        return new InfractionModel(id,
                type,
                victim.getUniqueId(),
                getIssuerUUID(),
                reason,
                duration,
                date,
                false);
    }
}
