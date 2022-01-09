package com.markiesch.utils;

import com.markiesch.EpicPunishments;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InputUtils {
    EpicPunishments plugin = EpicPunishments.getInstance();

    private final InputTypes chat;
    private int taskId;
    private final String title;
    private final String subtitle;
    private final Player player;

    public InputUtils(InputTypes chat, Player player, String title, String subtitle) {
        this.chat = chat;
        this.title = title;
        this.subtitle = subtitle;
        this.player = player;
        start();
    }

    public void start() {
        player.closeInventory();
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(EpicPunishments.getInstance(), () -> {
            if (player.isOnline() && !player.isDead()) {
                player.sendTitle(plugin.changeColor(title), plugin.changeColor(subtitle), 0, 51, 0);
            }
        }, 0L, 50L);
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(this.taskId);
        plugin.getEditor().remove(player.getUniqueId());
    }

    public InputTypes getChat() {
        return chat;
    }
}
