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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.markiesch.utils.BanMenuUtils.getConfigItemName;

public class PlayerSelectorMenu extends Menu {
    private final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);
    FileConfiguration config = plugin.getPlayerStorage().getConfig();
    int page;
    int maxPages;
    boolean onLastPage = true;
    int prevPageSlot = 45;
    int nextPageSlot = 53;
    int templateSlot = 52;
    int closeSlot = 49;

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
        if (e.getCurrentItem() == null) return;
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(
                                Objects.requireNonNull(
                                Objects.requireNonNull(
                                e.getCurrentItem().getItemMeta()).getPersistentDataContainer()
                                .get(new NamespacedKey(plugin, "uuid"), PersistentDataType.STRING))));

            if (e.getClick().toString().equalsIgnoreCase("right") && p.hasPermission("bangui.teleport")) {
                if (target.getPlayer() != null && target.getPlayer().isOnline()) {
                    p.setGameMode(GameMode.SPECTATOR);
                    p.teleport(target.getPlayer().getLocation());
                }
            } else {
                new PunishMenu(EpicPunishments.getPlayerMenuUtility(p), target).open();
            }
            return;
        }

        if (e.getSlot() == prevPageSlot && page != 0) new PlayerSelectorMenu(EpicPunishments.getPlayerMenuUtility(p), --page).open();
        if (e.getSlot() == nextPageSlot && !onLastPage) new PlayerSelectorMenu(EpicPunishments.getPlayerMenuUtility(p), ++page).open();
        if (e.getSlot() == templateSlot) new TemplatesMenu(EpicPunishments.getPlayerMenuUtility(p), 0).open();
        if (e.getSlot() == closeSlot) p.closeInventory();
    }

    @Override
    public void setMenuItems() {
        ItemStack closeButton = ItemUtils.createItem(Material.NETHER_STAR, "§c§lClose Menu", 1, "§7Click to close menu");
        inventory.setItem(closeSlot, closeButton);

        ItemStack templates = ItemUtils.createItem(Material.ANVIL, "§b§lTemplates", 1, "§7Click to manage templates");
        inventory.setItem(templateSlot, templates);

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

                OfflinePlayer target = players.get(index);

                if (target != null) {
                    Date date = new Date(target.getFirstPlayed());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    String formattedDate = sdf.format(date);
                    List<String> infractionsList = plugin.getPlayerStorage().getPunishments(target.getUniqueId());

                    ItemStack playerHead = ItemUtils.createItem(
                            Material.PLAYER_HEAD,
                            "§b§l" + target.getName(),
                            1,
                            "§bLeft Click §7to manage player",
                            "§bRight Click §7to teleport",
                            "",
                            (infractionsList.isEmpty() ? "§a✔ §7didn't received any punishments yet" : "§6✔ §7had received " + infractionsList.size() + " punishments"),
                            (plugin.getPlayerStorage().isPlayerBanned(target.getUniqueId()) ? "§6✔ §7" + target.getName() + " is §abanned §7on §e" + plugin.getServer().getName() : "§a✔ §a" + target.getName() + " §7is not §ebanned"),
                            "",
                            "§7Joined at: " + formattedDate
                    );

                    SkullMeta playerMeta = (SkullMeta) playerHead.getItemMeta();
                    if (playerMeta != null) {
                        playerMeta.setOwningPlayer(target);
                        playerMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "uuid"), PersistentDataType.STRING, target.getUniqueId().toString());
                        playerHead.setItemMeta(playerMeta);
                    }
                    inventory.setItem(headSlots[i], playerHead);
                }
            }
        }

        maxPages = players.size() / headSlots.length;

        if (page >= 1) {
            ItemStack prevPage = ItemUtils.createItem(Material.ARROW, getConfigItemName("mainMenu.prevPageName","§cPrevious Page"), 1, "§7Click to visit page " + page);
            inventory.setItem(prevPageSlot, prevPage);
        }

        if (page < maxPages) {
            ItemStack nextPage = ItemUtils.createItem(Material.ARROW, getConfigItemName("mainMenu.nextPageName","§cNext Page"), 1, "§7Click to visit page " + (page + 2));
            onLastPage = false;
            inventory.setItem(nextPageSlot, nextPage);
        }
    }
}
