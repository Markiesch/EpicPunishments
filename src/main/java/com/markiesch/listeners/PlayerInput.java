package com.markiesch.listeners;

import com.markiesch.EpicPunishments;
import com.markiesch.utils.InputTypes;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.menusystem.menus.CreateTemplateMenu;
import com.markiesch.menusystem.menus.EditTemplateMenu;
import com.markiesch.utils.InputUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerInput implements Listener {
    EpicPunishments plugin = EpicPunishments.getInstance();

    @EventHandler
    public void onInput(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getEditor().containsKey(player.getUniqueId())) return;

        InputUtils edit = plugin.getEditor().get(player.getUniqueId());
        String message = event.getMessage();
        event.setCancelled(true);
        edit.cancel();
        Bukkit.getScheduler().scheduleSyncDelayedTask(EpicPunishments.getInstance(), () -> {
            PlayerMenuUtility playerMenuUtility = EpicPunishments.getPlayerMenuUtility(player);

            if (edit.getChat().equals(InputTypes.CREATE_TEMPLATE_NAME)) {
                playerMenuUtility.setTemplateName(message.replace(" ", "_"));
                new CreateTemplateMenu(playerMenuUtility).open();
            } else if (edit.getChat().equals(InputTypes.CREATE_TEMPLATE_REASON)) {
                playerMenuUtility.setReason(message);
                new CreateTemplateMenu(playerMenuUtility).open();
            } else if (edit.getChat().equals(InputTypes.CREATE_TEMPLATE_DURATION)) {
                playerMenuUtility.setDuration(TimeUtils.parseTime(message.replace(" ", "")));
                new CreateTemplateMenu(playerMenuUtility).open();
            } else if (edit.getChat().equals(InputTypes.EDIT_TEMPLATE_NAME)) {
                playerMenuUtility.setTemplateName(message.replace(" ", "_"));
                new EditTemplateMenu(playerMenuUtility).open();
            } else if (edit.getChat().equals(InputTypes.EDIT_TEMPLATE_REASON)) {
                playerMenuUtility.setReason(message);
                new EditTemplateMenu(playerMenuUtility).open();
            } else if (edit.getChat().equals(InputTypes.EDIT_TEMPLATE_DURATION)) {
                playerMenuUtility.setDuration(TimeUtils.parseTime(message.replace(" ", "")));
                new EditTemplateMenu(playerMenuUtility).open();
            }
        }, 5L);
    }
}
