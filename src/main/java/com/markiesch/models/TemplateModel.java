package com.markiesch.models;

import com.markiesch.EpicPunishments;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class TemplateModel {
    private final EpicPunishments plugin;

    public final UUID id;
    public String name;
    public String reason;
    public String type;
    public long duration;

    public TemplateModel(UUID id, String name, String type, long duration, String reason) {
        this.plugin = EpicPunishments.getInstance();

        this.id = id;
        this.name = name;
        this.type = type;
        this.duration = duration;
        this.reason = reason;
    }

    public ItemStack getItemStack() {
        ItemStack item = ItemUtils.createItem(
            Material.PAPER,
            "§9§l" + name,
            "§bLeft Click §7to manage template",
            "§bPress Q §7to delete template",
            "",
            "§7Type: §a" + type,
            "§7Reason: §a" + (reason != null ? reason : "None"),
            "§7Duration: §a" + TimeUtils.makeReadable(duration)
        );

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "templateUUID"), PersistentDataType.STRING, id.toString());
        item.setItemMeta(meta);

        return item;
    }
}
