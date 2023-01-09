package com.markiesch.menusystem.menus;

import com.markiesch.Permission;
import com.markiesch.locale.Translation;
import com.markiesch.menusystem.Menu;
import com.markiesch.modules.profile.ProfileManager;
import com.markiesch.modules.profile.ProfileModel;
import com.markiesch.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerMenu extends Menu implements Listener {
    private static final byte DETAILS_SLOT = 13;
    private static final byte PUNISH_BUTTON_SLOT = 29;
    private static final byte INFRACTIONS_BUTTON_SLOT = 31;
    private static final byte IP_MATCHES_BUTTON_SLOT = 33;
    private static final byte BACK_BUTTON_SLOT = 49;

    public final ProfileModel target;

    public PlayerMenu(Plugin plugin, UUID uuid, UUID player) {
        super(plugin, uuid, 54);

        target = ProfileManager.getInstance().getPlayer(player);
        open();
    }

    @Override
    public String getMenuName() {
        return Translation.MENU_PLAYER_TITLE.addPlaceholder("name", target.getName()).toString();
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.MANAGE_PLAYERS;
    }

    public void setMenuItems() {
        if (target == null) {
            getOwner().sendMessage(Translation.MENU_PLAYER_NOT_FOUND.toString());
            getOwner().closeInventory();
            return;
        }

        ItemStack playerDetails = ItemUtils.createItem(
                Material.PLAYER_HEAD,
                Translation.MENU_PLAYER_DETAILS_TITLE.toString(),
                Translation.MENU_PLAYER_DETAILS_LORE
                        .addPlaceholder("name", target.getName())
                        .addPlaceholder("ip", target.ip)
                        .addPlaceholder("status", (target.getPlayer().isOnline() ? Translation.WORD_ONLINE : Translation.WORD_OFFLINE).toString())
                        .toList()
        );
        ItemUtils.setSkullOwner(playerDetails, target.getPlayer());
        setButton(DETAILS_SLOT, playerDetails, event -> new PunishMenu(plugin, uuid, target.uuid));

        ItemStack punishButton = ItemUtils.createItem(
                Material.ANVIL,
                Translation.MENU_PLAYER_PUNISH_TITLE.toString(),
                Translation.MENU_PLAYER_PUNISH_LORE.addPlaceholder("name", target.getName()).toList()
        );
        setButton(PUNISH_BUTTON_SLOT, punishButton, event -> new PunishMenu(plugin, uuid, target.uuid));

        ItemStack infractions = ItemUtils.createItem(
                Material.FLOWER_BANNER_PATTERN,
                Translation.MENU_PLAYER_INFRACTIONS_TITLE.toString(),
                Translation.MENU_PLAYER_INFRACTIONS_LORE.addPlaceholder("name", target.getName()).toList()
        );
        setButton(INFRACTIONS_BUTTON_SLOT, infractions, event -> new InfractionsMenu(plugin, uuid, target.uuid));

        List<ProfileModel> playersWithSameIp = ProfileManager.getInstance()
                .getPlayersUnderIp(target.ip)
                .stream()
                .filter(profile -> profile.uuid != target.uuid)
                .collect(Collectors.toList());

        String playerFormat =
                playersWithSameIp.isEmpty() ?
                        Translation.MENU_PLAYER_IP_MATCHES_EMPTY.toString() :
                        playersWithSameIp
                                .stream()
                                .map(profile -> Translation.MENU_PLAYER_IP_MATCHES_FORMAT
                                        .addPlaceholder("player_name", profile.getName())
                                        .toString()
                                )
                                .collect(Collectors.joining("\n"));

        ItemStack ipMatches = ItemUtils.createItem(
                Material.BOOKSHELF,
                Translation.MENU_PLAYER_IP_MATCHES_TITLE.toString(),
                Translation.MENU_PLAYER_IP_MATCHES_LORE.addPlaceholder("player_format", playerFormat).toList()
        );
        setButton(IP_MATCHES_BUTTON_SLOT, ipMatches);

        ItemStack back = ItemUtils.createItem(
                Material.OAK_SIGN,
                Translation.MENU_BACK_BUTTON_TITLE.toString(),
                Translation.MENU_BACK_BUTTON_LORE.toList()
        );
        setButton(BACK_BUTTON_SLOT, back, event -> new PlayerSelectorMenu(plugin, uuid));
    }
}