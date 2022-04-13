package com.markiesch.menusystem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerMenuUtility {
    private final UUID owner;
    private String name;
    private String reason;
    private String type;
    private Long duration;
    private UUID uuid;

    public PlayerMenuUtility(UUID owner) { this.owner = owner; }
    public Player getOwner() { return Bukkit.getPlayer(owner); }

    public String getTemplateName() { return name; }
    public void setTemplateName(String templateName) { this.name = templateName; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getType() { return type; }
    public void setType(String reason) { this.type = reason; }

    public Long getDuration() { return duration; }
    public void setDuration(Long duration) { this.duration = duration; }

    public UUID getUUID() { return uuid; }
    public void setUUID(UUID uuid) { this.uuid = uuid; }

    public void reset() {
        reason = null;
        name = null;
        type = null;
        duration = null;
        uuid = null;
    }

    public void fillEmptyFields() {
        name = name == null ? "new_template" : name;
        reason = reason == null ? "none" : reason;
        type = type == null ? "KICK" : type;
        duration = duration == null ? 0L : duration;
    }
}