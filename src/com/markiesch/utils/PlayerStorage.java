package com.markiesch.utils;

import com.markiesch.EpicPunishments;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getServer;


public class PlayerStorage {
    static final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);
    static long permanent = 0L;
    static FileConfiguration dataConfig = null;
    static File configFile = null;

    public static void reloadConfig() {
        // Create the data file if it doesn't exist already
        if (configFile == null) configFile = new File(plugin.getDataFolder(), "data.yml");
        dataConfig = YamlConfiguration.loadConfiguration(configFile);
        InputStream defaultStream = plugin.getResource("data.yml");

        if (defaultStream != null) {
            try {
                YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
                dataConfig.setDefaults(defaultConfig);
                defaultStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static FileConfiguration getConfig() {
        if (dataConfig == null) reloadConfig();
        return dataConfig;
    }

    public static void saveConfig() {
        if (dataConfig == null || configFile == null) return;

        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, e);
        }
    }

    public static void saveDefaultConfig() {
        if (configFile == null) configFile = new File(plugin.getDataFolder(), "data.yml");

        if (!configFile.exists()) plugin.saveResource("data.yml", false);
    }

    public static void createPlayerProfile(UUID uuid) {
        if (getConfig().contains(uuid.toString())) return;
        List<String> emptyPunishments = new ArrayList<>();
        getConfig().set(uuid + ".infractions", emptyPunishments);
        saveConfig();
        getServer().getConsoleSender().sendMessage("Successfully Registered " + Bukkit.getOfflinePlayer(uuid).getName());
    }

    public static boolean playerRegistered(UUID uuid) { return getConfig().getConfigurationSection(uuid.toString()) != null; }

    public static void createPunishment(UUID target, UUID issuer, PunishTypes type, String reason, Long duration) {
        if (getConfig().contains(target.toString())) createPlayerProfile(target);
        Player oTarget = Bukkit.getPlayer(target);
        if (reason.isEmpty()) reason = "none";

        if (type.equals(PunishTypes.KICK)) {
            if (oTarget != null) {
                String kickMessage = plugin.getConfig().getString("messages.kickMessage");
                if (kickMessage != null) {
                    kickMessage = kickMessage.replace("[reason]", reason);
                }
                oTarget.kickPlayer(plugin.changeColor(kickMessage));
            } else {
                Player oIssuer = Bukkit.getPlayer(issuer);
                if (oIssuer != null) oIssuer.sendMessage("§cThat player is not online. Warned instead!");
                type = PunishTypes.WARN;
            }
        }

        if (type.equals(PunishTypes.BAN) && oTarget != null) {
            if (duration.equals(permanent)) {
                String kickMessage = plugin.getConfig().getString("messages.permanentlyBanMessage");
                if (kickMessage != null) {
                    kickMessage = kickMessage
                            .replace("[duration]", TimeUtils.makeReadable(duration))
                            .replace("[reason]", reason);
                }
                oTarget.kickPlayer(kickMessage);
            } else {
                plugin.getConfig().getString("messages.temporarilyBanMessage");
                oTarget.kickPlayer("§cYou are temporarily banned for §f" + TimeUtils.makeReadable(duration) + " §cfrom this server!\n\n§7Reason: §f" + reason + "\n§7Find out more: §e§nwww.example.com");
            }
            oTarget.getWorld().spawnEntity(oTarget.getLocation(), EntityType.BAT);
        }

        if (type.equals(PunishTypes.WARN) && oTarget != null && oTarget.isOnline()) {
            String message = plugin.getConfig().getString("messages.warnMessage");
            if (message != null) oTarget.sendMessage(message.replace("[reason]", reason));
        }

        List<String> punishments = getConfig().getStringList(target + ".infractions");
        long currentTime = System.currentTimeMillis();
        long expires = currentTime + duration;
        String[] punishment = {
                issuer.toString(),
                type.toString(),
                reason,
                duration.toString(),
                Long.toString(expires)
        };

        punishments.add(String.join(";", punishment));
        getConfig().set(target + ".infractions", punishments);
        Player player = Bukkit.getPlayer(issuer);

        String sType = type.toString().toLowerCase(Locale.US);

        if ("kick".equals(sType)) sType = "kicked";
        if ("warn".equals(sType)) sType = "warned";
        if ("mute".equals(sType)) sType = "muted";
        if ("ban".equals(sType)) sType = "banned";

        if (player != null) player.sendMessage("§7Successfully " + sType + " §a" + Bukkit.getOfflinePlayer(target).getName() + " §7Reason: §e" + reason);
        saveConfig();
    }

    public static List<String> getPunishments(UUID target) {
        return getConfig().getStringList(target + ".infractions");
    }

    public static boolean isPlayerBanned(UUID uuid) {
        List<String> infractions = getConfig().getStringList(uuid + ".infractions");
        for (String infraction : infractions) {
            String type = infraction.split(";")[1];
            if (!"ban".equalsIgnoreCase(type)) continue;
            Long duration = Long.parseLong(infraction.split(";")[3]);
            if (duration.equals(permanent)) return true;
            long currentTime = System.currentTimeMillis();
            long expires = Long.parseLong(infraction.split(";")[4]);
            if (currentTime < expires) return true;
        }
        return false;
    }

    public static boolean isMuted(UUID uuid) {
        List<String> infractions = getConfig().getStringList(uuid + ".infractions");
        for (String infraction : infractions) {
            String type = infraction.split(";")[1];
            if (!"mute".equalsIgnoreCase(type)) continue;
            Long duration = Long.parseLong(infraction.split(";")[3]);
            if (duration.equals(permanent)) return true;
            long currentTime = System.currentTimeMillis();
            long expires = Long.parseLong(infraction.split(";")[4]);
            if (currentTime < expires) return true;
        }
        return false;
    }

    public static void unMute(UUID target) {
        List<String> infractions = getPunishments(target);
        List<String> newInfractions = infractions.stream().filter(infraction -> {
            String type  = infraction.split(";")[1];
            return (!"mute".equalsIgnoreCase(type) && isActivePunishment(infraction));
        }).collect(Collectors.toList());

        getConfig().set(target + ".infractions", newInfractions);
        saveConfig();
    }

    public static void unBan(UUID target) {
        List<String> infractions = getPunishments(target);
        List<String> newInfractions = infractions.stream().filter(infraction -> {
            String type  = infraction.split(";")[1];
            return (!"ban".equalsIgnoreCase(type) && isActivePunishment(infraction));
        }).collect(Collectors.toList());

        getConfig().set(target + ".infractions", newInfractions);
        saveConfig();
    }

    public static boolean isActivePunishment(String infraction) {
        Long duration = Long.parseLong(infraction.split(";")[3]);
        if (duration.equals(permanent)) return true;

        long expires = Long.parseLong(infraction.split(";")[4]);
        return System.currentTimeMillis() < expires;
    }

    public static void removeInfraction(UUID target, String data) {
        List<String> infractions = getPunishments(target);
        List<String> newInfractions = infractions.stream().filter(infraction -> !infraction.equals(data)).collect(Collectors.toList());

        getConfig().set(target + ".infractions", newInfractions);
        saveConfig();
    }
}
