package com.markiesch.modules.infraction;

import com.markiesch.modules.profile.ProfileController;
import com.markiesch.modules.profile.ProfileModel;
import com.markiesch.utils.InfractionType;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PreparedInfraction {
    public InfractionType type;
    public String reason;
    public long duration;

    public PreparedInfraction(InfractionType type, String reason, long duration) {
        this.type = type;
        this.reason = reason;
        this.duration = duration;
    }

    public PreparedInfraction(InfractionType type, String reason) {
        this.type = type;
        this.reason = reason;
        this.duration = 0L;
    }

    public void execute(Player issuer, OfflinePlayer target) {
        ProfileModel targetProfile = new ProfileController().getProfile(target.getUniqueId());

        switch (type) {
            case BAN:
                if (targetProfile.isBanned()) {
                    issuer.sendMessage("This player is already banned");
                    return;
                }

                // TODO check if target is online
                // TODO kick target

                break;
            case KICK:
                // TODO check if player is online
                if (!target.isOnline()) {
                    issuer.sendMessage("This player is not online");
                    return;
                }

                // TODO kick target
                Player onlineTarget = target.getPlayer();
                if (onlineTarget != null) {
                    onlineTarget.kickPlayer("You have been kicked: \nReason: " + reason);
                }

                break;
            case MUTE:
                sendMessage(target, "You have been muted: \nReason: " + reason);
                break;
            case WARN:
                sendMessage(target, "You have been warned: \nReason: " + reason);

                break;
        }

        new InfractionController().create(
                type,
                target.getUniqueId(),
                issuer.getUniqueId(),
                reason,
                duration
        );
    }

    private void sendMessage(OfflinePlayer offlineTarget, String message) {
        if (!offlineTarget.isOnline()) return;

        Player target = offlineTarget.getPlayer();
        if (target == null) return;

        target.sendMessage(message);
    }
}
