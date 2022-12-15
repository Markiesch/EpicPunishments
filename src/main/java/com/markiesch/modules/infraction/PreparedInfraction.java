package com.markiesch.modules.infraction;

import com.markiesch.Format;
import com.markiesch.locale.Translation;
import com.markiesch.modules.profile.ProfileManager;
import com.markiesch.modules.profile.ProfileModel;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PreparedInfraction {
    public final InfractionType type;
    public final CommandSender issuer;
    public final UUID victimUUID;
    public final String reason;
    public final long duration;
    public final long date;

    public PreparedInfraction(InfractionType type, CommandSender issuer, UUID victimUUID, String reason, long duration) {
        this.type = type;
        this.issuer = issuer;
        this.victimUUID = victimUUID;
        this.reason = reason;
        this.duration = duration;
        date = System.currentTimeMillis() / 1000L;
    }

    public void execute() {
        ProfileModel victimProfile = ProfileManager.getInstance().getPlayer(victimUUID);

        if (victimProfile == null) {
            issuer.sendMessage(Translation.COMMAND_PLAYER_NOT_FOUND.addPlaceholder("name", "").toString());
            return;
        }

        OfflinePlayer victim = victimProfile.getPlayer();
        InfractionList victimInfractionList = InfractionManager.getInstance().getPlayer(victimUUID);

        switch (type) {
            case KICK -> {
                if (!victim.isOnline()) {
                    issuer.sendMessage(Translation.EVENT_KICK_OFFLINE.addPlaceholder("victim_name", victimProfile.getName()).toString());
                    return;
                }
                Player onlineTarget = victim.getPlayer();
                if (onlineTarget != null) {
                    onlineTarget.kickPlayer(Format.KICK.getString(this));

                    issuer.sendMessage(Translation.EVENT_KICK_SUCCESS.addPlaceholder("victim_name", victimProfile.getName()).toString());
                }
            }
            case MUTE -> {
                if (victimInfractionList.isMuted()) {
                    issuer.sendMessage(
                            Translation.EVENT_MUTE_ALREADY
                                    .addPlaceholder("victim_name", victimProfile.getName())
                                    .toString());
                    return;
                }
                issuer.sendMessage(Translation.EVENT_MUTE_SUCCESS.addPlaceholder("victim_name", victimProfile.getName()).toString());
            }
            case BAN -> {
                if (victimInfractionList.isBanned()) {
                    issuer.sendMessage(Translation.EVENT_BAN_ALREADY.addPlaceholder("victim_name", victimProfile.getName()).toString());
                    return;
                }

                if (victim.isOnline()) {
                    Player onlineVictim = victim.getPlayer();
                    if (onlineVictim != null) {
                        String message = this.isPermanent() ?
                                Format.PERMANENTLY_BANNED.getString(this) :
                                Format.TEMPORARILY_BANNED.getString(this);
                        onlineVictim.kickPlayer(message);
                    }

                    issuer.sendMessage(Translation.EVENT_BAN_SUCCESS.addPlaceholder("victim_name", victim.getName()).toString());
                }
            }
        }

        boolean success = InfractionManager.getInstance().createInfraction(this);
        // TODO send error message if success is false\
    }

    public InfractionModel createInfraction(int id) {
        return new InfractionModel(id,
                type,
                victimUUID,
                getIssuerUUID(),
                reason,
                duration,
                date,
                false);
    }

    private @Nullable UUID getIssuerUUID() {
        return issuer instanceof OfflinePlayer ? ((Player) issuer).getUniqueId() : null;
    }

    private boolean isPermanent() {
        return duration == 0L;
    }
}
