package com.markiesch.modules.infraction;

import com.markiesch.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class InfractionModel {
    public int id;
    public InfractionType type;
    public UUID victim;
    @Nullable public UUID issuer;
    public String reason;
    public long duration;
    public long date;
    public boolean revoked;

    public InfractionModel(int id, InfractionType type, UUID victim, @Nullable UUID issuer, String reason, long duration, long date, boolean revoked) {
        this.id = id;
        this.type = type;
        this.victim = victim;
        this.issuer = issuer;
        this.reason = reason;
        this.duration = duration;
        this.date = date;
        this.revoked = revoked;
    }

    public String getIssuer() {
        return this.issuer == null ? "Console" : Bukkit.getOfflinePlayer(issuer).getName();
    }

    public boolean isActive() {
        if (revoked) return false;
        if (duration == 0L) return true;

        long currentUnixTime = System.currentTimeMillis() / 1000L;
        return (date + duration > currentUnixTime);
    }

    public boolean isPermanent() {
        return duration == 0L;
    }

    public void setRevoked(boolean value) {
        revoked = value;
    }

    public String getFormattedDuration() {
        return TimeUtils.makeReadable(this.duration);
    }

    public long getDuration() {
        return duration;
    }

    public long getTimeLeft() {
        if (duration == 0L) return 0L;

        long currentUnixTime = System.currentTimeMillis() / 1000L;
        return date + duration - currentUnixTime;
    }

    public String getReason() {
        return reason;
    }
}
