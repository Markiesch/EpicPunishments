package com.markiesch.modules.template;

import com.markiesch.modules.infraction.InfractionType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TemplateManager {
    private final List<TemplateModel> templates = new ArrayList<>();

    private TemplateManager() {
    }

    public void initialize() {
        templates.addAll(new TemplateController().readAll());
    }

    public boolean create(String name, String reason, InfractionType type, long duration) {
         TemplateModel templateModel = new TemplateController().create(name, reason, type, duration);
         if (templateModel == null) return false;

         templates.add(templateModel);
         return true;
    }

    private static class TemplateManagerHolder {
        public static final TemplateManager INSTANCE = new TemplateManager();
    }

    public static TemplateManager getInstance() {
        return TemplateManagerHolder.INSTANCE;
    }

    public List<TemplateModel> getTemplates() {
        return templates;
    }

    public @Nullable TemplateModel getTemplate(int id) {
        return templates
                .stream()
                .filter(template -> template.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean update(int id, String name, InfractionType type, String reason, long duration) {
        int affectedRows = new TemplateController().update(id, name, type, reason, duration);
        if (affectedRows == 0) return false;

        templates.stream()
                .filter(templateModel -> templateModel.getId() == id)
                .findFirst()
                .ifPresent(templateModel -> {
                    templateModel.setName(name);
                    templateModel.setType(type);
                    templateModel.setReason(reason);
                    templateModel.setDuration(duration);
                });
        return true;
    }

    public boolean delete(int id) {
        int affectedRows = new TemplateController().delete(id);
        if (affectedRows == 0) return false;

        templates.removeIf(templateModel -> templateModel.getId() == id);
        return true;
    }
}
