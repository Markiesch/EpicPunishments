package com.markiesch.modules.infraction;

import com.markiesch.modules.profile.ProfileController;
import com.markiesch.modules.profile.ProfileModel;
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
                if (!target.isOnline()) {
                    issuer.sendMessage("This player is not online");
                    return;
                }

                Player onlineTarget = target.getPlayer();
                if (onlineTarget != null) {
                    onlineTarget.kickPlayer("You have been kicked: \nReason: " + reason);
                }

                break;
            case MUTE:
                if (targetProfile.isMuted()) {
                    issuer.sendMessage("§e" + target.getName() + "§7 is already§c muted");
                    return;
                }

                issuer.sendMessage("§7Successfully muted §e" + target.getName());
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
}
