package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.Menu;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.PlayerStorage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import static com.markiesch.utils.BanMenuUtils.getConfigItemName;

public class PlayerSelectorMenu extends Menu {
    EpicPunishments plugin = EpicPunishments.getInstance();
    private int page;
    private boolean onLastPage = true;
    private final int prevPageSlot = 45;
    private final int nextPageSlot = 53;
    private final int templateSlot = 52;
    private final int closeSlot = 49;

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
    public void handleMenu(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
            ItemMeta meta = event.getCurrentItem().getItemMeta();
            if (meta == null) return;

            String uuid = meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "uuid"), PersistentDataType.STRING);
            if (uuid == null) return;

            OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
            if ("right".equalsIgnoreCase(event.getClick().toString()) && player.hasPermission("epicpunishments.teleport")) {
                if (target.getPlayer() != null && target.getPlayer().isOnline()) {
                    player.setGameMode(GameMode.SPECTATOR);
                    player.teleport(target.getPlayer().getLocation());
                }
            } else {
                new PunishMenu(EpicPunishments.getPlayerMenuUtility(player), target).open();
            }
            return;
        }

        if (event.getSlot() == prevPageSlot && page != 0) new PlayerSelectorMenu(EpicPunishments.getPlayerMenuUtility(player), --page).open();
        if (event.getSlot() == nextPageSlot && !onLastPage) new PlayerSelectorMenu(EpicPunishments.getPlayerMenuUtility(player), ++page).open();
        if (event.getSlot() == templateSlot) new TemplatesMenu(EpicPunishments.getPlayerMenuUtility(player), 0).open();
        if (event.getSlot() == closeSlot) player.closeInventory();
    }

    @Override
    public void setMenuItems() {
        ItemStack closeButton = ItemUtils.createItem(Material.NETHER_STAR, "§c§lClose Menu", "§7Click to close menu");
        inventory.setItem(closeSlot, closeButton);

        ItemStack templates = ItemUtils.createItem(Material.ANVIL, "§b§lTemplates", "§7Click to manage templates");
        inventory.setItem(templateSlot, templates);

        ConfigurationSection configSection = PlayerStorage.getConfig().getConfigurationSection("");
        if (configSection == null) return;

        List<OfflinePlayer> players = new ArrayList<>();
        for (String uuid : configSection.getKeys(false)) players.add(Bukkit.getOfflinePlayer(UUID.fromString(uuid)));
        int[] headSlots = { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34 };

        if (!players.isEmpty()) {
            for (int i = 0; i < headSlots.length; i++) {
                int index = headSlots.length * page + i;
                if (index >= players.size()) break;

                OfflinePlayer target = players.get(index);

                if (target != null) {
                    Date date = new Date(target.getFirstPlayed());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    String formattedDate = sdf.format(date);
                    List<String> infractionsList = PlayerStorage.getPunishments(target.getUniqueId());

                    ItemStack playerHead = ItemUtils.createItem(
                            Material.PLAYER_HEAD,
                            "§b§l" + target.getName(),
                            "§bLeft Click §7to manage player",
                            "§bRight Click §7to teleport",
                            "",
                            (infractionsList.isEmpty() ? "§a✔ §7didn't received any punishments yet" : "§6✔ §7had received " + infractionsList.size() + " punishments"),
                            (PlayerStorage.isPlayerBanned(target.getUniqueId()) ? "§6✔ §7" + target.getName() + " is §abanned §7on §e" + plugin.getServer().getName() : "§a✔ §a" + target.getName() + " §7is not §ebanned"),
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

        int maxPages = players.size() / headSlots.length;

        if (page >= 1) {
            ItemStack prevPage = ItemUtils.createItem(Material.ARROW, getConfigItemName("mainMenu.prevPageName","§cPrevious Page"), "§7Click to visit page " + page);
            inventory.setItem(prevPageSlot, prevPage);
        }

        if (page < maxPages) {
            ItemStack nextPage = ItemUtils.createItem(Material.ARROW, getConfigItemName("mainMenu.nextPageName","§cNext Page"), "§7Click to visit page " + (page + 2));
            onLastPage = false;
            inventory.setItem(nextPageSlot, nextPage);
        }
    }
}
