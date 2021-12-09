package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.InputTypes;
import com.markiesch.menusystem.Menu;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.utils.InputUtils;
import com.markiesch.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class TemplatesMenu extends Menu implements Listener {
    private static final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);
    int page;
    int maxTemplatesPerPage = 28;
    boolean onLastPage = true;

    public TemplatesMenu(PlayerMenuUtility playerMenuUtility, int currentPage) {
        super(playerMenuUtility);
        page = currentPage;
    }

    @Override
    public String getMenuName() {
        return "Templates";
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

        if (clickedItem.equals(Material.PAPER)) {
            ItemMeta meta = e.getCurrentItem().getItemMeta();
            String template;
            if (meta != null) {
                template = ChatColor.stripColor(meta.getDisplayName());
                new EditTemplateMenu(playerMenuUtility, template).open();
            } else {
                player.sendMessage("§cThere was an error trying to resolve the template name");
            }
        }

        if (clickedItem.equals(Material.ANVIL))
            plugin.getEditor().put(uuid, new InputUtils(InputTypes.TEMPLATE_NAME, player, "§bNew Template", "§7Type in a template name"));

        if (clickedItem.equals(Material.OAK_SIGN))
            new PlayerSelectorMenu(EpicPunishments.getPlayerMenuUtility(player), 0).open();
    }
    @Override
    public void setMenuItems() {
        ItemStack back = ItemUtils.createItem(Material.OAK_SIGN, "§b§lBack", 1, "§7Click to go back");
        inventory.setItem(49, back);

        ItemStack newTemplate = ItemUtils.createItem(Material.ANVIL, "§b§lNew template", 1, "§7Click to create a new template");
        inventory.setItem(52, newTemplate);

        generateTemplates();
    }

    private void generateTemplates() {
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        ConfigurationSection configurationSection = plugin.getTemplateStorage().getConfig().getConfigurationSection("");
        if (configurationSection == null) {
            playerMenuUtility.getOwner().sendMessage("There was an error whilst opening the Templates Menu");
            return;
        }
        ArrayList<String> templates = new ArrayList<>(configurationSection.getKeys(false));
        if (templates.isEmpty()) {
            ItemStack noTemplates = ItemUtils.createItem(Material.MAP, "§6§lNo Templates!", 1, "§7There are no templates yet!");
            inventory.setItem(22, noTemplates);
            return;
        }
        for (int i = 0; i < maxTemplatesPerPage; i++) {
            int index = maxTemplatesPerPage * page + i;
            if (index >= templates.size()) break;
            if (templates.get(index) != null) {
                String type = plugin.getTemplateStorage().getConfig().getString(templates.get(i) + ".type");
                if (type != null) type = type.substring(0, 1).toUpperCase(Locale.US) + type.substring(1).toLowerCase(Locale.US);
                String reason = plugin.getTemplateStorage().getConfig().getString(templates.get(i) + ".reason");
                reason = reason != null && reason.length() > 13 ? reason.substring(0, 10) + "..."  : reason;

                ItemStack template = ItemUtils.createItem(Material.PAPER, "§9§l" + templates.get(i), 1,
                        "§bLeft Click §7to manage template", "", "§7Type: §a" + type, "§7Reason: §a" + (reason != null ? reason : "None"));

                inventory.setItem(slots[i], template);
            }
        }
    }
}
