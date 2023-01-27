package com.markiesch.menusystem.menus;

import com.markiesch.Permission;
import com.markiesch.chat.PlayerChat;
import com.markiesch.locale.Translation;
import com.markiesch.menusystem.PaginatedModelMenu;
import com.markiesch.menusystem.PlayerSelectorSearchType;
import com.markiesch.modules.infraction.InfractionManager;
import com.markiesch.modules.infraction.InfractionModel;
import com.markiesch.modules.profile.ProfileManager;
import com.markiesch.modules.profile.ProfileModel;
import com.markiesch.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerSelectorMenu extends PaginatedModelMenu<ProfileModel> {
    private PlayerSelectorSearchType filter;
    private String nameFilter = "";

    private static final byte FILTER_SLOT = 46;
    private static final byte SEARCH_SLOT = 47;
    private static final byte CLOSE_SLOT = 49;
    private static final byte TEMPLATE_BUTTON_SLOT = 52;

    public PlayerSelectorMenu(Plugin plugin, UUID uuid) {
        super(
                plugin,
                uuid,
                54,
                new byte[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34}
        );
        this.filter = PlayerSelectorSearchType.ALL;

        open();
    }

    @Override
    public String getMenuName() {
        return Translation.MENU_PLAYERS_TITLE.toString();
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.MANAGE_PLAYERS;
    }

    @Override
    protected ItemStack modelToItemStack(ProfileModel profile) {
        List<InfractionModel> infractionsList = InfractionManager.getInstance().getPlayer(profile.uuid);

        ItemStack playerHead = ItemUtils.createItem(
                Material.PLAYER_HEAD,
                Translation.MENU_PLAYERS_BUTTON_PLAYER_TITLE
                        .addPlaceholder("player_name", profile.name)
                        .toString(),
                Translation.MENU_PLAYERS_BUTTON_PLAYER_LORE
                        .addPlaceholder("punishments_size", infractionsList.size())
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
            playerMeta.setOwningPlayer(profile.getPlayer());
            playerHead.setItemMeta(playerMeta);
        }

        return playerHead;
    }

    @Override
    protected @NotNull List<ProfileModel> getModels() {
        return ProfileManager.getInstance()
                .getPlayers()
                .stream()
                .filter(profile -> {
                    if (!profile.getName().toLowerCase(Locale.ROOT).contains(nameFilter.toLowerCase(Locale.ROOT)))
                        return false;

                    if (filter.equals(PlayerSelectorSearchType.ALL)) return true;
                    else if (filter.equals(PlayerSelectorSearchType.ONLINE_ONLY) && profile.getPlayer().isOnline())
                        return true;
                    else return filter.equals(PlayerSelectorSearchType.OFFLINE_ONLY) && !profile.getPlayer().isOnline();
                })
                .collect(Collectors.toList());
    }

    @Override
    protected void handleModelClick(InventoryClickEvent event, ProfileModel profile) {
        new PlayerMenu(plugin, uuid, profile.getPlayer().getUniqueId());
    }

    @Override
    public void setMenuItems() {
        super.setMenuItems();

        String nextFilter = Translation.WORD_ALL.getRawString();
        if (filter.equals(PlayerSelectorSearchType.ALL)) nextFilter = Translation.WORD_ONLINE.getRawString();
        else if (filter.equals(PlayerSelectorSearchType.ONLINE_ONLY))
            nextFilter = Translation.WORD_OFFLINE.getRawString();


        ItemStack filterItem = ItemUtils.createItem(
                Material.ENDER_EYE,
                Translation.MENU_PLAYERS_BUTTON_FILTER_TITLE.toString(),
                Translation.MENU_PLAYERS_BUTTON_FILTER_LORE.addPlaceholder("next_filter", nextFilter).toList()
        );
        setButton(
                FILTER_SLOT,
                filterItem,
                event -> toggleFilter()
        );

        ItemStack searchItem = ItemUtils.createItem(
                Material.COMPASS,
                Translation.MENU_PLAYERS_BUTTON_NAME_FILTER_TITLE.toString(),
                Translation.MENU_PLAYERS_BUTTON_NAME_FILTER_LORE.addPlaceholder("current_filter", nameFilter.equals("") ? "none" : nameFilter).toList()
        );

        setButton(SEARCH_SLOT, searchItem, (event) -> {
            new PlayerChat(plugin, getOwner(), Translation.MENU_PLAYERS_INSERT_NAME_FILTER_TITLE.toString(), Translation.MENU_PLAYERS_INSERT_NAME_FILTER_SUBTITLE.toString(), message -> {
                nameFilter = message;
                open();
            });
        });

        ItemStack closeButton = ItemUtils.createItem(
                Material.NETHER_STAR,
                Translation.MENU_CLOSE_BUTTON_TITLE.toString(),
                Translation.MENU_CLOSE_BUTTON_LORE.toList()
        );
        setButton(CLOSE_SLOT, closeButton, (event) -> getOwner().closeInventory());

        ItemStack templatesButton = ItemUtils.createItem(
                Material.ANVIL,
                Translation.MENU_PLAYERS_TEMPLATES_BUTTON_TITLE.toString(),
                Translation.MENU_PLAYERS_TEMPLATES_BUTTON_LORE.toList()
        );
        setButton(TEMPLATE_BUTTON_SLOT, templatesButton, (event) -> new TemplateSelectorMenu(plugin, uuid));
    }

    public void toggleFilter() {
        if (filter.equals(PlayerSelectorSearchType.ALL)) filter = PlayerSelectorSearchType.ONLINE_ONLY;
        else if (filter.equals(PlayerSelectorSearchType.ONLINE_ONLY)) filter = PlayerSelectorSearchType.OFFLINE_ONLY;
        else if (filter.equals(PlayerSelectorSearchType.OFFLINE_ONLY)) filter = PlayerSelectorSearchType.ALL;

        getInventory().remove(Material.PLAYER_HEAD);
        setMenuItems();
    }
}