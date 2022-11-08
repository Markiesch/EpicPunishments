package com.markiesch.listeners;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.menusystem.menus.CreateTemplateMenu;
import com.markiesch.utils.InputTypes;
import com.markiesch.utils.InputUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerInput implements Listener {
    private final EpicPunishments plugin;

    public PlayerInput(EpicPunishments plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInput(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getEditor().containsKey(player.getUniqueId())) return;

        InputUtils edit = plugin.getEditor().get(player.getUniqueId());
        String message = event.getMessage();
        event.setCancelled(true);
        edit.cancel();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            PlayerMenuUtility playerMenuUtility = plugin.getPlayerMenuUtility(player.getUniqueId());

            if (edit.getChat().equals(InputTypes.CREATE_TEMPLATE_NAME)) {
                playerMenuUtility.setTemplateName(message.replace(" ", "_"));
                new CreateTemplateMenu(plugin, playerMenuUtility);
            } else if (edit.getChat().equals(InputTypes.CREATE_TEMPLATE_REASON)) {
                playerMenuUtility.setReason(message);
                new CreateTemplateMenu(plugin, playerMenuUtility);
            } else if (edit.getChat().equals(InputTypes.CREATE_TEMPLATE_DURATION)) {
                playerMenuUtility.setTemplateDuration(TimeUtils.parseTime(message.replace(" ", "")));
                new CreateTemplateMenu(plugin, playerMenuUtility);
            }
        }, 5L);
    }
}
