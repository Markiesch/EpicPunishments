package com.markiesch.menusystem;

import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerMenuUtility {
    private final Player owner;
    private String reason;
    private String templateName;
    private UUID uuid;

    public PlayerMenuUtility(Player owner) { this.owner = owner; }
    public Player getOwner() { return owner; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getTemplateName() { return templateName; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }

    public UUID getUUID() { return uuid; }
    public void setUUID(UUID uuid) { this.uuid = uuid; }
}
