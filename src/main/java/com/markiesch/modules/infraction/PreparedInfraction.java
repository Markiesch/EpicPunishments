package com.markiesch.modules.infraction;

import com.markiesch.modules.profile.ProfileController;
import com.markiesch.modules.profile.ProfileModel;
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
        ProfileModel targetProfile = new ProfileController().getProfile(victim.getUniqueId());

        InfractionList victimInfractionList = InfractionManager.getInstance().getPlayer(victim.getUniqueId());

        switch (type) {
            case BAN -> {
                if (victimInfractionList.isBanned()) {
                    issuer.sendMessage("This player is already banned");
                    return;
                }

                // TODO check if target is online
                // TODO kick target
                if (victim.isOnline()) {
                    Player onlineVictim = victim.getPlayer();
                    if (onlineVictim != null) {
                        onlineVictim.kickPlayer("You have been banned: \nReason:" + reason);
                    }
                }
            }
            case KICK -> {
                if (!victim.isOnline()) {
                    issuer.sendMessage("This player is not online");
                    return;
                }
                Player onlineTarget = victim.getPlayer();
                if (onlineTarget != null) {
                    onlineTarget.kickPlayer("You have been kicked: \nReason: " + reason);
                }
            }
            case MUTE -> {
                if (victimInfractionList.isMuted()) {
                    issuer.sendMessage("§e" + victim.getName() + "§7 is already§c muted");
                    return;
                }
                issuer.sendMessage("§7Successfully muted §e" + victim.getName());
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
                date);
    }
}
