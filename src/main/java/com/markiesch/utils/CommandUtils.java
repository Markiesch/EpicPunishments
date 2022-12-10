package com.markiesch.utils;

import com.markiesch.modules.profile.ProfileManager;
import com.markiesch.modules.profile.ProfileModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CommandUtils {
    public static List<String> getAllOfflinePlayerNames() {
        return ProfileManager.getInstance().getPlayers().stream().map(ProfileModel::getName).collect(Collectors.toList());
    }

    public static List<String> getAllOnlinePlayerNames() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    public static List<String> getTimeOptions(String input) {
        if (input.length() > 0 && Character.isDigit(input.charAt(input.length() - 1))) {
            return List.of(
                    input + "s",
                    input + "m",
                    input + "d",
                    input + "y");
        }

        return List.of(
                input.contains("s") ? input : input + "30s",
                input.contains("m") ? input : input + "15m",
                input.contains("d") ? input : input + "5d",
                input.contains("y") ? input : input + "1y");
    }
}
