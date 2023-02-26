package com.markiesch.modules.warning;

import com.markiesch.Format;
import com.markiesch.modules.category.CategoryManager;
import com.markiesch.modules.category.CategoryModel;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class WarningModel {
    private final int id;
    private final @Nullable Integer categoryId;
    private final @Nullable String reason;
    private final UUID victim;
    private final UUID issuer;
    private boolean seen;

    public WarningModel(int id, @Nullable Integer categoryId, @Nullable String reason, @NotNull UUID victim, @Nullable UUID issuer, boolean seen) {
        this.id = id;
        this.categoryId = categoryId;
        this.reason = reason;
        this.victim = victim;
        this.issuer = issuer;
        this.seen = seen;
    }

    public int getId() {
        return id;
    }

    public @Nullable Integer getCategoryId() {
        return categoryId;
    }

    public @Nullable CategoryModel getCategory() {
        if (this.getCategoryId() == null) return null;
        return CategoryManager.getInstance().getCategoryById(this.getCategoryId());
    }

    public UUID getVictim() {
        return victim;
    }

    public UUID getIssuer() {
        return issuer;
    }

    public @Nullable String getReason() {
        return reason;
    }

    public String getIssuerName() {
        return issuer == null ? "Console" : Bukkit.getOfflinePlayer(issuer).getName();
    }

    public boolean hasSeenMessage() {
        return seen;
    }

    public String getFormat() {
        Format format = getCategory() == null || getCategory().getMessage() == null ? Format.REASON_WARNING : Format.MESSAGE_WARNING;
        return format.getString(this);
    }

    public void setSeen(boolean value) {
        this.seen = value;
    }
}
