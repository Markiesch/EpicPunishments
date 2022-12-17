package com.markiesch.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Base64;
import java.util.NoSuchElementException;

public abstract class SkullTexture {
    public static @Nullable String fromPlayerUUID(Player player) {
        GameProfile gameProfile = PlayerUtils.getGameProfile(player);
        if (gameProfile == null) return null;
        try {
            String value = gameProfile.getProperties()
                    .get("textures")
                    .iterator()
                    .next()
                    .getValue();
            return textureDataToUrl(value);
        } catch (NoSuchElementException noSuchElementException) {
            return null;
        }
    }

    public static String fromMojangAPI(String playerName) {
        try {
            String profileURL = "https://api.mojang.com/users/profiles/minecraft/" + playerName;
            String uuid = JsonUtils.readJsonFromUrl(profileURL)
                    .get("id")
                    .getAsString();

            String textureURL = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false";

            String value = JsonUtils.readJsonFromUrl(textureURL)
                    .get("properties")
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject()
                    .get("value")
                    .getAsString();
            return textureDataToUrl(value);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static String textureDataToUrl(String texture) {
        String json = new String(Base64.getDecoder().decode(texture));

        Gson gson = new Gson();
        return gson.fromJson(json, JsonObject.class).get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();
    }
}
