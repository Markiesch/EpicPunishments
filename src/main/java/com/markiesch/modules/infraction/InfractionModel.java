package com.markiesch.modules.infraction;

import java.util.UUID;

public class InfractionModel {
    public final int id;
    public InfractionType type;
    public UUID victim;
    public UUID issuer;
    public String reason;
    public long duration;
    public long date;

    public InfractionModel(int id, InfractionType type, UUID victim, UUID issuer, String reason, long duration, long date) {
        this.id = id;
        this.type = type;
        this.victim = victim;
        this.issuer = issuer;
        this.reason = reason;
        this.duration = duration;
        this.date = date;
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
