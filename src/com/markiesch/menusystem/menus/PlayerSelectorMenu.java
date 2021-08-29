package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.Menu;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.markiesch.utils.BanMenuUtils.generatePlayerMeta;
import static com.markiesch.utils.BanMenuUtils.getConfigItem;
import static com.markiesch.utils.BanMenuUtils.getConfigItemName;

public class PlayerSelectorMenu extends Menu {
    private final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);
    FileConfiguration config = plugin.getPlayerStorage().getConfig();
    int page;
    int maxPages;
    boolean onLastPage = true;

    public ItemStack closeButton;
    public PlayerSelectorMenu(PlayerMenuUtility playerMenuUtility, int currentPage) {
        super(playerMenuUtility);
        page = currentPage;
    }

    @Override
    public String getMenuName() {
        return "Overview > Players";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        // Return when the player doesn't click on an item
        if (e.getCurrentItem() == null) return;
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(
                                Objects.requireNonNull(
                                Objects.requireNonNull(
                                e.getCurrentItem().getItemMeta()).getPersistentDataContainer()
                                .get(new NamespacedKey(plugin, "uuid"), PersistentDataType.STRING))));

            if (e.getClick().toString().equalsIgnoreCase("right") && p.hasPermission("bangui.teleport")) {
                // on right click Teleport to player
                if (target.getPlayer() != null && target.getPlayer().isOnline()) {
                    p.setGameMode(GameMode.SPECTATOR);
                    p.teleport(target.getPlayer().getLocation());
                }
            } else {
                new PunishMenu(EpicPunishments.getPlayerMenuUtility(p), target).open();
            }
            return;
        }

        if (e.getSlot() == 45 && page != 0) new PlayerSelectorMenu(EpicPunishments.getPlayerMenuUtility(p), --page).open();
        if (e.getSlot() == 53 && !onLastPage) new PlayerSelectorMenu(EpicPunishments.getPlayerMenuUtility(p), ++page).open();
        if (e.getSlot() == 52) new TemplatesMenu(EpicPunishments.getPlayerMenuUtility(p), 0).open();
        if (e.getCurrentItem().getType() == Material.matchMaterial(closeButton.getType().toString())) p.closeInventory();
    }

    @Override
    public void setMenuItems() {
        boolean addFiller = plugin.getConfig().getBoolean("mainMenu.addFiller");
        boolean addClose = plugin.getConfig().getBoolean("mainMenu.addClose");

        if (addFiller) generateFiller();
        if (addClose) generateCloseButton();
        ItemStack templates = new ItemStack(Material.ANVIL);
        ItemMeta templatesMeta = templates.getItemMeta();
        if (templatesMeta != null) {
            templatesMeta.setDisplayName(plugin.changeColor("§b§lTemplates"));
            ArrayList<String> lore = new ArrayList<>();
            lore.add(plugin.changeColor("§7Click to manage templates"));
            templatesMeta.setLore(lore);
            templates.setItemMeta(templatesMeta);
        }

        inventory.setItem(52, templates);


        int[] headSlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        List<OfflinePlayer> players = new ArrayList<>();
        ConfigurationSection configSection = config.getConfigurationSection("");
        if (configSection == null) return;
        for (String uuid : configSection.getKeys(false)) {
            players.add(Bukkit.getOfflinePlayer(UUID.fromString(uuid)));
        }

        if (!players.isEmpty()) {
            for (int i = 0; i < headSlots.length; i++) {
                int index = headSlots.length * page + i;
                if (index >= players.size()) break;
                if (players.get(index) != null) {
                    ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
                    SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
                    generatePlayerMeta(meta, players.get(index), playerHead);
                    inventory.setItem(headSlots[i], playerHead);
                }
            }
        }

        maxPages = players.size() / headSlots.length;

        if (page >= 1) {
            ItemStack prevPage = ItemUtils.createItem(Material.ARROW, getConfigItemName("mainMenu.prevPageName","§cPrevious Page"), 1, "§7Click to visit page " + page);
            inventory.setItem(45, prevPage);
        }

        if (page < maxPages) {
            ItemStack nextPage = ItemUtils.createItem(Material.ARROW, getConfigItemName("mainMenu.nextPageName","§cNext Page"), 1, "§7Click to visit page " + (page + 2));
            onLastPage = false;
            inventory.setItem(53, nextPage);
        }
    }

    public void generateFiller() {
        ItemStack filler = new ItemStack(getConfigItem("mainMenu.fillerItem", "LIGHT_GRAY_STAINED_GLASS_PANE"), 1);
        ItemMeta fillerMeta = filler.getItemMeta();
        if (fillerMeta != null) {
            fillerMeta.setDisplayName("§f");
            filler.setItemMeta(fillerMeta);
        }
        int[] filterSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 50, 51, 52, 53};
        for (int filterSlot : filterSlots) inventory.setItem(filterSlot, filler);
    }

    public void generateCloseButton() {
        closeButton = new ItemStack(getConfigItem("mainMenu.closeItem", "LIGHT_GRAY_STAINED_GLASS_PANE"), 1);
        ItemMeta closeButtonMeta = closeButton.getItemMeta();
        if (closeButtonMeta != null) {
            closeButtonMeta.setDisplayName(getConfigItemName("mainMenu.closeName","&c&lClose Menu"));
            closeButton.setItemMeta(closeButtonMeta);
        }
        inventory.setItem(49, closeButton);
    }
}
