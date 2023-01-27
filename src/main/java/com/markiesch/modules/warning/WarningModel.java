package com.markiesch.modules.warning;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class WarningModel {
    private final int id;
    private final int categoryId;
    private final UUID victim;
    private final UUID issuer;

    public WarningModel(int id, int categoryId, @NotNull UUID victim, @Nullable UUID issuer) {
        this.id = id;
        this.categoryId = categoryId;
        this.victim = victim;
        this.issuer = issuer;
    }

    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public UUID getVictim() {
        return victim;
    }

    public UUID getIssuer() {
        return issuer;
    }

    public String getIssuerName() {
        return issuer == null ? "Console" : Bukkit.getOfflinePlayer(issuer).getName();
    }
}
