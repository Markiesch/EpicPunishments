package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.locale.Translation;
import com.markiesch.menusystem.PaginatedModelMenu;
import com.markiesch.menusystem.PlayerSelectorSearchType;
import com.markiesch.modules.infraction.InfractionController;
import com.markiesch.modules.infraction.InfractionModel;
import com.markiesch.modules.profile.ProfileController;
import com.markiesch.modules.profile.ProfileModel;
import com.markiesch.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerSelectorMenu extends PaginatedModelMenu<ProfileModel> {
    private final EpicPunishments plugin;
    private final ProfileController profileController;
    private final InfractionController infractionController;
    private PlayerSelectorSearchType filter;

    private final byte TEMPLATE_BUTTON_SLOT = 52;

    private final int closeSlot = 49;
    private final int filterSlot = 46;

    public PlayerSelectorMenu(EpicPunishments plugin, UUID uuid, int page) {
        super(
                plugin,
                uuid,
                54,
                new int[] { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34 }
        );
        this.plugin = plugin;
        this.filter = PlayerSelectorSearchType.ALL;
        this.page = page;
        profileController = new ProfileController();
        infractionController = new InfractionController();

        open();
    }

    @Override
    public String getMenuName() {
        return Translation.MENU_PLAYERS_TITLE.toString();
    }

    @Override
    public String getRequiredPermission() {
        return "epicpunishments.selector";
    }

    @Override
    protected ItemStack modelToItemStack(ProfileModel profile) {
        OfflinePlayer target = profile.getPlayer();

        long date = profile.getPlayer().getFirstPlayed();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.US);
        String formattedDate = sdf.format(date);
        List<InfractionModel> infractionsList = infractionController.readAll(target.getUniqueId());

        ItemStack playerHead = ItemUtils.createItem(
                Material.PLAYER_HEAD,
                Translation.MENU_PLAYERS_BUTTON_PLAYER_TITLE
                        .addPlaceholder("player_name",  profile.getPlayer().getName())
                        .toString(),
                Translation.MENU_PLAYERS_BUTTON_PLAYER_LORE
                        .addPlaceholder("punishments_size", infractionsList.size())
                        .addPlaceholder("player_first_join", formattedDate)
                        .addPlaceholder(
                                "punishments_lore",
                                (infractionsList.size() == 0 ?
                                        Translation.MENU_PLAYERS_BUTTON_PLAYER_LORE_PUNISHMENTS_EMPTY :
                                        Translation.MENU_PLAYERS_BUTTON_PLAYER_LORE_PUNISHMENTS_NOT_EMPTY)
                                            .addPlaceholder("punishments_size", infractionsList.size())
                                            .toString())
                        .toList()
        );

        SkullMeta playerMeta = (SkullMeta) playerHead.getItemMeta();
        if (playerMeta != null) {
            playerMeta.setOwningPlayer(target);
            playerMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "uuid"), PersistentDataType.STRING, target.getUniqueId().toString());
            playerHead.setItemMeta(playerMeta);
        }

        return playerHead;
    }

    @Override
    protected List<ProfileModel> getModels() {
        return profileController.getProfiles().stream()
                .filter(profile -> {
                    if (filter.equals(PlayerSelectorSearchType.ALL)) return true;
                    else if (filter.equals(PlayerSelectorSearchType.ONLINE_ONLY) && profile.getPlayer().isOnline()) return true;
                    else return filter.equals(PlayerSelectorSearchType.OFFLINE_ONLY) && !profile.getPlayer().isOnline();
                }).collect(Collectors.toList());
    }

    @Override
    protected void handleModelClick(InventoryClickEvent event, ProfileModel profile) {
        new PlayerMenu(plugin, uuid, profile.getPlayer().getUniqueId());
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        super.handleMenu(event);

        switch (event.getSlot()) {
            case TEMPLATE_BUTTON_SLOT -> new TemplateSelectorMenu(plugin, uuid);
            case closeSlot -> getOwner().closeInventory();
            case filterSlot -> toggleFilter();
        }
    }

    @Override
    public void setMenuItems() {
        super.setMenuItems();

        String nextFilter = "all";
        if (filter.equals(PlayerSelectorSearchType.ALL)) nextFilter = "online";
        else if (filter.equals(PlayerSelectorSearchType.ONLINE_ONLY)) nextFilter = "offline";

        ItemStack filterItem = ItemUtils.createItem(Material.ENDER_EYE,
                Translation.MENU_PLAYERS_BUTTON_FILTER_TITLE.toString(),
                Translation.MENU_PLAYERS_BUTTON_FILTER_LORE.addPlaceholder("next_filter", nextFilter).toList());
        getInventory().setItem(filterSlot, filterItem);

        ItemStack closeButton = ItemUtils.createItem(Material.NETHER_STAR, Translation.MENU_CLOSE_BUTTON_TITLE.toString(), Translation.MENU_CLOSE_BUTTON_LORE.toList());
        getInventory().setItem(closeSlot, closeButton);

        ItemStack templates = ItemUtils.createItem(Material.ANVIL, Translation.MENU_PLAYERS_TEMPLATES_BUTTON_TITLE.toString(), Translation.MENU_PLAYERS_TEMPLATES_BUTTON_LORE.toList());
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