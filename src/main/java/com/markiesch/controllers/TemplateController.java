package com.markiesch.controllers;

import com.markiesch.models.TemplateModel;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class TemplateController extends ConfigController {
    public TemplateController() {
        super("templates.yml");
    }

    public List<TemplateModel> readAll() {
        List<TemplateModel> result = new ArrayList<>();

        ConfigurationSection configurationSection = getConfigurationSection("");
        if (configurationSection == null) return result;

        List<String> templateUUIDs = new ArrayList<>(configurationSection.getKeys(false));

        for (String templateUUID : templateUUIDs) {
            UUID uuid = UUID.fromString(templateUUID);

            String name = getConfig().getString(templateUUID + ".name");

            String type = getConfig().getString(templateUUID + ".type");
            if (type != null) type = type.substring(0, 1).toUpperCase(Locale.US) + type.substring(1).toLowerCase(Locale.US);

            String reason = getConfig().getString(templateUUID + ".reason");
            reason = reason != null && reason.length() > 30 ? reason.substring(0, 27) + "..."  : reason;
            long duration = getConfig().getLong(templateUUID + ".duration");

            TemplateModel template = new TemplateModel(uuid, name, type, duration, reason);
            result.add(template);
        }

        return result;
    }

    public void addTemplate(String name, String reason, String type, long duration) {
        UUID uuid = getTemplateUUID();
        ConfigurationSection section = getConfig().createSection(uuid.toString());
        section.set("name", name);
        section.set("type", type);
        section.set("duration", duration);
        section.set("reason", reason);
        saveConfig();
    }

    public boolean removeTemplate(UUID uuid) {
        ConfigurationSection section = getConfig().getConfigurationSection(uuid.toString());
        if (section == null) return false;
        getConfig().set(uuid.toString(), null);
        saveConfig();
        return true;
    }

    public boolean editTemplate(UUID uuid, String name, String reason, String type, long duration) {
        ConfigurationSection section = getConfig().getConfigurationSection(uuid.toString());
        if (section == null) return false;

        section.set("name", name);
        section.set("type", type);
        section.set("duration", duration);
        section.set("reason", reason);

        saveConfig();
        return true;
    }

    private UUID getTemplateUUID() {
        UUID uuid = UUID.randomUUID();
        if (getConfig().get(uuid.toString()) != null) return getTemplateUUID();
        return uuid;
    }

    public UUID getUUIDFromName(String name) {
        ConfigurationSection section = getConfig().getConfigurationSection("");
        if (section == null) return null;

        UUID templateUuid = null;
        for (String uuid : section.getKeys(false)) {
            if (name.equals(getConfig().getString(uuid + ".name"))) templateUuid = UUID.fromString(uuid);
        }

        return templateUuid;
    }
}
