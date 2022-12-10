package com.markiesch.utils;

import org.bukkit.Bukkit;

public abstract class Reflection {
    public static Class<?> getCraftClass(String paramString) {
        return getClass("org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().substring(23) + "." + paramString);
    }

    public static Class<?> getClass(String paramString) {
        try {
            return Class.forName(paramString);
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
