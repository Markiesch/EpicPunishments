package com.markiesch.modules.categoryRule;

import com.markiesch.modules.template.TemplateManager;
import com.markiesch.modules.template.TemplateModel;

public class CategoryRuleModel {
    private final int id;
    private final int categoryId;
    private int templateId;
    private int count;

    public CategoryRuleModel(int id, int categoryId, int templateId, int count) {
        this.id = id;
        this.categoryId = categoryId;
        this.templateId = templateId;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getTemplateId() {
        return templateId;
    }

    public String getTemplateName() {
        TemplateModel template = TemplateManager.getInstance().getTemplate(templateId);
        return template == null ? "-" : template.name;
    }

    public void setTemplateId(int id) {
        this.templateId = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
