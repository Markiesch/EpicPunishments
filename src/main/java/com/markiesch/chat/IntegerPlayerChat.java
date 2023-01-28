package com.markiesch.chat;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public class IntegerPlayerChat {
    public IntegerPlayerChat(Plugin plugin, Player player, String title, String subtitle, Consumer<Integer> finishCallback) {
        new PlayerChat(plugin, player, title, subtitle, (message) -> {
            try {
                finishCallback.accept(Integer.parseInt(message));
            } catch (NumberFormatException exception) {
                finishCallback.accept(null);
            }
        });
    }
}
