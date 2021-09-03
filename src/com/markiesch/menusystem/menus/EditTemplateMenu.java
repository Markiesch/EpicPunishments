package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.InputTypes;
import com.markiesch.menusystem.Menu;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.utils.InputUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EditTemplateMenu extends Menu implements Listener {
    private final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);

    private final String name;
    private String type = "KICK";

    public EditTemplateMenu(PlayerMenuUtility playerMenuUtility, String name) {
        super(playerMenuUtility);
        playerMenuUtility.setTemplateName(name);
        this.name = name;
        ConfigurationSection section = plugin.getTemplateStorage().getConfig().getConfigurationSection(name);
        if (section == null) {
            plugin.getTemplateStorage().addTemplate("KICK", playerMenuUtility.getOwner());
        } else {
            this.type = section.getString("type");
        }
    }

    @Override
    public String getMenuName() {
        return "Templates > New Template";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        Player player = (Player) e.getWhoClicked();
        UUID uuid = player.getUniqueId();
        Material clickedItem = e.getCurrentItem().getType();

        if (clickedItem.equals(Material.WRITABLE_BOOK)) {
            plugin.getEditor().put(uuid, new InputUtils(InputTypes.TEMPLATE_REASON, player, "§bTemplate Reason", "§7Type in a template reason"));
            return;
        }

        if (clickedItem.equals(Material.EMERALD_BLOCK)) {
            plugin.getTemplateStorage().addTemplate(type, player);
            player.closeInventory();
            player.sendMessage("§7Successfully §acreated §7a new template with the name of §e" + name);
            return;
        }

        if (e.getSlot() == 19) {
            ItemStack item = e.getCurrentItem();
            ItemMeta meta = item.getItemMeta();
            if (meta == null) return;
            String name = ChatColor.stripColor(meta.getDisplayName());
            if (name.equalsIgnoreCase("BAN")) type = "KICK";
            if (name.equalsIgnoreCase("KICK")) type = "WARN";
            if (name.equalsIgnoreCase("WARN")) type = "MUTE";
            if (name.equalsIgnoreCase("MUTE")) type = "BAN";
            setMenuItems();
        }
    }

    @Override
    public void setMenuItems() {
        String reason = playerMenuUtility.getReason();
        if (reason == null) reason = "None";

        ItemStack template = new ItemStack(Material.PAPER);
        ItemMeta templateMeta = template.getItemMeta();

        if (templateMeta != null) {
            templateMeta.setDisplayName("§c§l" + name);
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add("§cType: §7" + type);
            lore.add("§cReason: §7" + reason);
            templateMeta.setLore(lore);
            template.setItemMeta(templateMeta);
        }
        inventory.setItem(13, template);

        Material type = Material.PAPER;
        if (this.type.equalsIgnoreCase("BAN")) type = Material.OAK_DOOR;
        if (this.type.equalsIgnoreCase("KICK")) type = Material.ENDER_EYE;
        if (this.type.equalsIgnoreCase("WARN")) type = Material.PAPER;
        if (this.type.equalsIgnoreCase("MUTE")) type = Material.STRING;

        ItemStack typeItem = new ItemStack(type);
        ItemMeta typeMeta = typeItem.getItemMeta();

        if (typeMeta != null) {
            typeMeta.setDisplayName("§c§l" + this.type);
            List<String> lore = new ArrayList<>();
            lore.add("§7Click to toggle type");
            typeMeta.setLore(lore);
            typeItem.setItemMeta(typeMeta);
        }
        inventory.setItem(19, typeItem);

        ItemStack reasonItem = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta reasonMeta = reasonItem.getItemMeta();

        if (reasonMeta != null) {
            reasonMeta.setDisplayName("§c§lReason");
            List<String> lore = new ArrayList<>();
            lore.add("§7Click to insert reason");
            lore.add("");
            lore.add("§7Reason set: §c" + reason);
            reasonMeta.setLore(lore);
            reasonItem.setItemMeta(reasonMeta);
        }
        inventory.setItem(25, reasonItem);

        ItemStack createItem = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta createMeta = createItem.getItemMeta();

        if (createMeta != null) {
            createMeta.setDisplayName("§c§lCreate Template");
            List<String> lore = new ArrayList<>();
            lore.add("§7Click to confirm settings");
            lore.add("");
            lore.add("§7Reason set: §c" + reason);
            createMeta.setLore(lore);
            createItem.setItemMeta(createMeta);
        }
        inventory.setItem(40, createItem);
    }
}
