package com.markiesch.menusystem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerMenuUtility {
    private final UUID owner;
    private String templateName;
    private String templateReason;
    private String templateType;
    private Long templateDuration;

    public PlayerMenuUtility(UUID owner) { this.owner = owner; }
    public Player getOwner() { return Bukkit.getPlayer(owner); }

    public String getTemplateName() { return templateName; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }

    public String getReason() { return templateReason; }
    public void setReason(String templateReason) { this.templateReason = templateReason; }

    public String getType() { return templateType; }
    public void setType(String templateType) { this.templateType = templateType; }

    public Long getTemplateDuration() { return templateDuration; }
    public void setTemplateDuration(Long templateDuration) { this.templateDuration = templateDuration; }

    public void reset() {
        templateReason = null;
        templateName = null;
        templateType = null;
        templateDuration = null;
    }

    public void fillEmptyFields() {
        templateName = templateName == null ? "new_template" : templateName;
        templateReason = templateReason == null ? "none" : templateReason;
        templateType = templateType == null ? "KICK" : templateType;
        templateDuration = templateDuration == null ? 0L : templateDuration;
    }
}