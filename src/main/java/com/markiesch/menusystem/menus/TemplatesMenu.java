package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.models.TemplateModel;
import com.markiesch.utils.*;
import com.markiesch.menusystem.Menu;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.menusystem.SearchTypes;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public class TemplatesMenu extends Menu implements Listener {
    private static final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);
    private int page;
    private boolean onLastPage = true;
    private final int prevPageSlot = 45;
    private final int nextPageSlot = 53;

    public TemplatesMenu(PlayerMenuUtility playerMenuUtility, int currentPage) {
        super(playerMenuUtility, "Templates > Overview", 54);
        if (!hasPermission()) return;
        page = currentPage;
        open();
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

            if (event.getClick().equals(ClickType.DROP) && player.hasPermission("epicpunishments.templates.manage")) {
                EpicPunishments.getTemplateController().removeTemplate(uuid);
                getInventory().remove(Material.PAPER);
                setMenuItems();
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
        getInventory().setItem(49, back);

        if (playerMenuUtility.getOwner().hasPermission("epicpunishments.templates.manage")) {
            ItemStack newTemplate = ItemUtils.createItem(Material.ANVIL, "§b§lNew template", "§7Click to create a new template");
            getInventory().setItem(52, newTemplate);
        }

        // =========================
        // ======= Templates =======
        // =========================
        List<TemplateModel> templates = EpicPunishments.getTemplateController().readAll();
        if (templates.isEmpty()) {
            ItemStack noTemplates = ItemUtils.createItem(Material.MAP, "§6§lNo Templates!", "§7There are no templates yet!");
            getInventory().setItem(22, noTemplates);
            return;
        }

        int[] slots = { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34 };
        templates = templates.subList(slots.length * page, Math.min(slots.length * page + slots.length, templates.size()));

        for (int i = 0; i < templates.size(); i++) {
            getInventory().setItem(slots[i], templates.get(i).getItemStack());
        }

        // ==========================
        // ======= Pagination =======
        // ==========================
        int maxPages = templates.size() / slots.length;
        if (page >= 1) {
            ItemStack prevPage = ItemUtils.createItem(Material.ARROW, "§cPrevious Page", "§7Click to visit page " + page);
            getInventory().setItem(prevPageSlot, prevPage);
        }

        if (page < maxPages) {
            ItemStack nextPage = ItemUtils.createItem(Material.ARROW, "§cNext Page", "§7Click to visit page " + (page + 2));
            onLastPage = false;
            getInventory().setItem(nextPageSlot, nextPage);
        }
    }

    public boolean hasPermission() {
        Player player = playerMenuUtility.getOwner();
        if (player.hasPermission("epicpunishments.templates")) return true;
        player.sendMessage("§7You do not have§c permissions§7 to view or manage templates");
        player.closeInventory();
        return false;
    }
}
