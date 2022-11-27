package com.markiesch.modules.infraction;

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

    public InfractionModel(int id, InfractionType type, UUID victim, @Nullable UUID issuer, String reason, long duration, long date) {
        this.id = id;
        this.type = type;
        this.victim = victim;
        this.issuer = issuer;
        this.reason = reason;
        this.duration = duration;
        this.date = date;
    }

    public String getIssuer() {
        return this.issuer == null ? "Console" : Bukkit.getOfflinePlayer(issuer).getName();
    }

    public boolean isActive() {
        if (duration == 0L) return true;

        long currentUnixTime = System.currentTimeMillis() / 1000L;
        return (date + duration > currentUnixTime);
    }

    public boolean isPermanent() {
        return duration == 0L;
    }
}
