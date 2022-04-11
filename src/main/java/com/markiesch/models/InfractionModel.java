package com.markiesch.models;

import com.markiesch.utils.PunishTypes;

import java.util.Date;
import java.util.UUID;

public class InfractionModel {
    public final UUID uuid;
    public PunishTypes type;
    public UUID victim;
    public UUID issuer;
    public String reason;
    public long duration;
    public Date date;

    public InfractionModel(UUID uuid, PunishTypes type, UUID victim, UUID issuer, String reason, long duration, Date date) {
        this.uuid = uuid;
        this.type = type;
        this.victim = victim;
        this.issuer = issuer;
        this.reason = reason;
        this.duration = duration;
        this.date = date;
    }

    public boolean isActive() {
        return System.currentTimeMillis() > date.getTime() + duration;
    }
}
