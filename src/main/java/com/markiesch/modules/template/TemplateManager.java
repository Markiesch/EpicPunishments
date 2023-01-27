package com.markiesch.modules.template;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TemplateManager {
    private final List<TemplateModel> templates = new ArrayList<>();

    private TemplateManager() {}

    public void initialize() {
        templates.addAll(new TemplateController().readAll());
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
                .filter(template -> template.id == id)
                .findFirst()
                .orElse(null);
    }
}
