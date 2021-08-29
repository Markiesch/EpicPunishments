package com.markiesch.menusystem;

import org.bukkit.entity.Player;

public class PlayerMenuUtility {
    private Player owner;
    private String reason;
    private String templateName;

    public PlayerMenuUtility(Player owner) {
        this.owner = owner;
    }
    public Player getOwner() {
        return owner;
    }
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
