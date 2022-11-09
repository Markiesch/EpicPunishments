package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.modules.infraction.InfractionController;
import com.markiesch.modules.profile.ProfileController;
import com.markiesch.menusystem.PaginatedMenu;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.menusystem.PlayerSelectorSearchType;
import com.markiesch.modules.infraction.InfractionModel;
import com.markiesch.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerSelectorMenu extends PaginatedMenu {
    private final EpicPunishments plugin;
    private final ProfileController profileController;
    private final InfractionController infractionController;
    private PlayerSelectorSearchType filter;

    private final byte TEMPLATE_BUTTON_SLOT = 52;

    private final int closeSlot = 49;
    private final int filterSlot = 46;

    public PlayerSelectorMenu(EpicPunishments plugin, PlayerMenuUtility playerMenuUtility, int page, PlayerSelectorSearchType filter) {
        super(
                plugin,
                playerMenuUtility,
                54,
                new int[] { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34 }
        );
        this.plugin = plugin;
        this.filter = filter;
        this.page = page;
        profileController = new ProfileController();
        infractionController = new InfractionController();

        open();
    }

    @Override
    public String getMenuName() {
        return "Overview > Players";
    }

    @Override
    public String getRequiredPermission() {
        return "epicpunishments.selector";
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        super.handleMenu(event);
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
                new PunishMenu(plugin, plugin.getPlayerMenuUtility(player.getUniqueId()), target.getUniqueId());
            }
            return;
        }

        if (event.getSlot() == TEMPLATE_BUTTON_SLOT) new TemplateSelectorMenu(plugin, playerMenuUtility);
        if (event.getSlot() == closeSlot) player.closeInventory();
        if (event.getSlot() == filterSlot) toggleFilter();
    }

    @Override
    public void setMenuItems() {
        List<ItemStack> players = profileController.getProfiles().stream()
                .filter(profile -> {
                    if (filter.equals(PlayerSelectorSearchType.ALL)) return true;
                    else if (filter.equals(PlayerSelectorSearchType.ONLINE_ONLY) && profile.getPlayer().isOnline()) return true;
                    else return filter.equals(PlayerSelectorSearchType.OFFLINE_ONLY) && !profile.getPlayer().isOnline();
                })
                .map(profile -> {
                    OfflinePlayer target = profile.getPlayer();

                    String headName = "§b§l" + profile.getPlayer().getName();

                    long date = profile.getPlayer().getFirstPlayed();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    String formattedDate = sdf.format(date);
                    List<InfractionModel> infractionsList = infractionController.readAll(target.getUniqueId());

                    ItemStack playerHead = ItemUtils.createItem(
                            Material.PLAYER_HEAD,
                            headName,
                            "§bLeft Click §7to manage player",
                            "§bRight Click §7to teleport",
                            "",
                            (infractionsList.isEmpty() ? "§a✔ §7didn't received any punishments yet" : "§6✔ §7had received " + infractionsList.size() + " punishments"),
                            // (PlayerStorage.isPlayerBanned(target.getUniqueId()) ? "§6✔ §7" + target.getName() + " is§a banned §7on §e" + plugin.getServer().getName() : "§a✔ §a" + target.getName() + " §7is not§e banned"),
                            "",
                            "§7Joined at: " + formattedDate
                    );

                    SkullMeta playerMeta = (SkullMeta) playerHead.getItemMeta();
                    if (playerMeta != null) {
                        playerMeta.setOwningPlayer(target);
                        playerMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "uuid"), PersistentDataType.STRING, target.getUniqueId().toString());
                        playerHead.setItemMeta(playerMeta);
                    }

                    return playerHead;
                })
                .collect(Collectors.toList());

        setPaginatedItems(players);

        String nextFilter = "all";
        if (filter.equals(PlayerSelectorSearchType.ALL)) nextFilter = "online";
        else if (filter.equals(PlayerSelectorSearchType.ONLINE_ONLY)) nextFilter = "offline";

        ItemStack filterItem = ItemUtils.createItem(Material.ENDER_EYE, "§b§lVisibility", "§7Click to show §e" + nextFilter + " §7users");
        getInventory().setItem(filterSlot, filterItem);

        ItemStack closeButton = ItemUtils.createItem(Material.NETHER_STAR, "§c§lClose", "§7Click to close menu");
        getInventory().setItem(closeSlot, closeButton);

        ItemStack templates = ItemUtils.createItem(Material.ANVIL, "§b§lTemplates", "§7Click to manage templates");
        getInventory().setItem(TEMPLATE_BUTTON_SLOT, templates);
    }

    public void toggleFilter() {
        if (filter.equals(PlayerSelectorSearchType.ALL)) filter = PlayerSelectorSearchType.ONLINE_ONLY;
        else if (filter.equals(PlayerSelectorSearchType.ONLINE_ONLY)) filter = PlayerSelectorSearchType.OFFLINE_ONLY;
        else if (filter.equals(PlayerSelectorSearchType.OFFLINE_ONLY)) filter = PlayerSelectorSearchType.ALL;

        getInventory().remove(Material.PLAYER_HEAD);
        setMenuItems();
    }
}