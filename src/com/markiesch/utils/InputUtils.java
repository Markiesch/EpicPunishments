package com.markiesch.utils;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.InputTypes;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InputUtils {
    private final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);

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
                player.sendTitle(plugin.changeColor(title), plugin.changeColor(subtitle), 0, 26, 0);
            }
        }, 0L, 25L);
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(this.taskId);
    }

    public InputTypes getChat() {
        return chat;
    }
}
