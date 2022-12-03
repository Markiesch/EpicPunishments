package com.markiesch.modules.infraction;

import org.bukkit.Material;

public enum InfractionType {
    BAN(Material.OAK_DOOR),
    KICK(Material.ENDER_EYE),
    MUTE(Material.STRING);

    private final Material material;

    InfractionType(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public InfractionType getNextType() {
        if (this == InfractionType.BAN) return InfractionType.KICK;
        else if (this == InfractionType.KICK) return InfractionType.MUTE;
        else if (this == InfractionType.MUTE) return InfractionType.BAN;

        return InfractionType.BAN;
    }
}
