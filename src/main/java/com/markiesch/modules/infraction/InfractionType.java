package com.markiesch.modules.infraction;

import com.markiesch.locale.Translation;
import org.bukkit.Material;

public enum InfractionType {
    BAN(Material.OAK_DOOR, Translation.WORD_BANNED),
    KICK(Material.ENDER_EYE, Translation.WORD_KICKED),
    MUTE(Material.STRING, Translation.WORD_MUTED);

    private final Material material;
    private final Translation translation;

    InfractionType(Material material, Translation translation) {
        this.material = material;
        this.translation = translation;
    }

    public Material getMaterial() {
        return material;
    }

    public Translation getTranslation() {
        return translation;
    }

    public InfractionType getNextType() {
        if (this == InfractionType.BAN) return InfractionType.KICK;
        else if (this == InfractionType.KICK) return InfractionType.MUTE;
        else if (this == InfractionType.MUTE) return InfractionType.BAN;

        return InfractionType.BAN;
    }
}
