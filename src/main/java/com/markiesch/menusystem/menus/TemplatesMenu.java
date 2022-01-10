package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.utils.InputTypes;
import com.markiesch.menusystem.Menu;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.menusystem.SearchTypes;
import com.markiesch.utils.InputUtils;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.TemplateStorage;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class TemplatesMenu extends Menu implements Listener {
    private static final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);
    private int page;
    private boolean onLastPage = true;
    private final int prevPageSlot = 45;
    private final int nextPageSlot = 53;

    public TemplatesMenu(PlayerMenuUtility playerMenuUtility, int currentPage) {
        super(playerMenuUtility);
        if (!hasPermission()) return;
        page = currentPage;
        open();
    }

    public String getMenuName() {
        return "Templates";
    }

    public int getSlots() {
        return 54;
    }

    public void handleMenu(InventoryClickEvent event) {
        if (event.getCurrentItem() == null || !hasPermission()) return;
        Player player = (Player) event.getWhoClicked();
        Material clickedItem = event.getCurrentItem().getType();

        if (clickedItem.equals(Material.PAPER)) {
            ItemMeta meta = event.getCurrentItem().getItemMeta();
            if (meta == null) return;

            String uuidString = meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "templateUUID"), PersistentDataType.STRING);
            if (uuidString == null) return;

            UUID uuid = UUID.fromString(uuidString);

            if (event.getClick().equals(ClickType.DROP)) {
                if (player.hasPermission("epicpunishments.templates.manage")) {
                    TemplateStorage.removeTemplate(uuid);
                    inventory.remove(Material.PAPER);
                    setMenuItems();
                } else player.sendMessage("§7You do not have§c permissions§7 to delete templates");

                return;
            }

            playerMenuUtility.reset();
            playerMenuUtility.setUUID(uuid);
            new EditTemplateMenu(playerMenuUtility);
        }

        if (event.getSlot() == prevPageSlot && page != 0) new TemplatesMenu(EpicPunishments.getPlayerMenuUtility(player), --page);
        if (event.getSlot() == nextPageSlot && !onLastPage) new TemplatesMenu(EpicPunishments.getPlayerMenuUtility(player), ++page);
        if (clickedItem.equals(Material.ANVIL)) plugin.getEditor().put(player.getUniqueId(), new InputUtils(InputTypes.CREATE_TEMPLATE_NAME, player, "§bNew Template", "§7Type in a template name"));
        if (clickedItem.equals(Material.OAK_SIGN)) new PlayerSelectorMenu(EpicPunishments.getPlayerMenuUtility(player), 0, SearchTypes.ALL);
    }

    public void setMenuItems() {
        ItemStack back = ItemUtils.createItem(Material.OAK_SIGN, "§b§lBack", "§7Click to go back");
        inventory.setItem(49, back);

        if (playerMenuUtility.getOwner().hasPermission("epicpunishments.templates.manage")) {
            ItemStack newTemplate = ItemUtils.createItem(Material.ANVIL, "§b§lNew template", "§7Click to create a new template");
            inventory.setItem(52, newTemplate);
        }

        generateTemplates();
    }

    private void generateTemplates() {
        ConfigurationSection configurationSection = TemplateStorage.getConfig().getConfigurationSection("");
        if (configurationSection == null) {
            playerMenuUtility.getOwner().sendMessage("There was an error whilst opening the Templates Menu");
            return;
        }
        List<String> templates = new ArrayList<>(configurationSection.getKeys(false));
        if (templates.isEmpty()) {
            ItemStack noTemplates = ItemUtils.createItem(Material.MAP, "§6§lNo Templates!", "§7There are no templates yet!");
            inventory.setItem(22, noTemplates);
            return;
        }

        int[] slots = { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34 };

        for (int i = 0; i < slots.length; i++) {
            int index = slots.length * page + i;
            if (index >= templates.size()) break;
            if (templates.get(index) == null) continue;
            String type = TemplateStorage.getConfig().getString(templates.get(index) + ".type");
            if (type != null) type = type.substring(0, 1).toUpperCase(Locale.US) + type.substring(1).toLowerCase(Locale.US);
            String reason = TemplateStorage.getConfig().getString(templates.get(index) + ".reason");
            reason = reason != null && reason.length() > 30 ? reason.substring(0, 27) + "..."  : reason;
            String name = TemplateStorage.getConfig().getString(templates.get(index) + ".name");
            long duration = TemplateStorage.getConfig().getLong(templates.get(index) + ".duration");

            ItemStack template = ItemUtils.createItem(Material.PAPER, "§9§l" + name,
                    "§bLeft Click §7to manage template", "§bPress Q §7to delete template", "", "§7Type: §a" + type, "§7Reason: §a" + (reason != null ? reason : "None"), "§7Duration: §a" + TimeUtils.makeReadable(duration));

            ItemMeta meta = template.getItemMeta();
            String uuid = templates.get(index);
            if (meta != null && uuid != null) {
                meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "templateUUID"), PersistentDataType.STRING, uuid);
                template.setItemMeta(meta);
            }

            inventory.setItem(slots[i], template);
        }

        int maxPages = templates.size() / slots.length;
        if (page >= 1) {
            ItemStack prevPage = ItemUtils.createItem(Material.ARROW, "§cPrevious Page", "§7Click to visit page " + page);
            inventory.setItem(prevPageSlot, prevPage);
        }

        if (page < maxPages) {
            ItemStack nextPage = ItemUtils.createItem(Material.ARROW, "§cNext Page", "§7Click to visit page " + (page + 2));
            onLastPage = false;
            inventory.setItem(nextPageSlot, nextPage);
        }
    }

    public boolean hasPermission() {
        Player player = playerMenuUtility.getOwner();
        if (player.hasPermission("epicpunishments.templates")) return true;
        player.sendMessage("§7You do not have§c permissions§7 to view templates");
        player.closeInventory();
        return false;
    }
}
