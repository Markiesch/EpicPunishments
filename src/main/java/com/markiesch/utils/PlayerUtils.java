package com.markiesch.utils;

import com.mojang.authlib.GameProfile;
import org.bukkit.entity.Player;

public abstract class PlayerUtils {
    private static final Class<?> CRAFT_PLAYER = Reflection.getCraftClass("entity.CraftPlayer");

    private static Object getCraftPlayer(Player player) {
        return CRAFT_PLAYER.cast(player);
    }

    public static GameProfile getGameProfile(Player player) {
        Object object = getCraftPlayer(player);
        try {
            return (GameProfile) CRAFT_PLAYER.getMethod("getProfile").invoke(object, new Object[0]);
        } catch (IllegalAccessException | java.lang.reflect.InvocationTargetException |
                 NoSuchMethodException illegalAccessException) {
            illegalAccessException.printStackTrace();
            return null;
        }
    }
}
