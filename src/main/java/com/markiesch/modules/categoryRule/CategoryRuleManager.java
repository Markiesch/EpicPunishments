package com.markiesch.modules.categoryRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryRuleManager {
    private final Map<Integer, List<CategoryRuleModel>> categoryRulesMap;

    private CategoryRuleManager() {
        categoryRulesMap = new HashMap<>();
    }

    private static class CategoryRulesManagerHolder {
        public static final CategoryRuleManager INSTANCE = new CategoryRuleManager();
    }

    public static CategoryRuleManager getInstance() {
        return CategoryRulesManagerHolder.INSTANCE;
    }

    public void initialize() {
        List<CategoryRuleModel> categoryRules = new CategoryRuleController().readAll();

        for (CategoryRuleModel categoryRule : categoryRules) {
            add(categoryRule);
        }
    }

    private void add(CategoryRuleModel categoryRule) {
        categoryRulesMap.computeIfAbsent(categoryRule.getCategoryId(), k -> new ArrayList<>()).add(categoryRule);
    }

    public List<CategoryRuleModel> getCategoryRules(int categoryId) {
        return categoryRulesMap.getOrDefault(categoryId, new ArrayList<>());
    }

    public boolean create(int categoryId, int templateId, int count) {
        CategoryRuleModel categoryRule = new CategoryRuleController().create(categoryId, templateId, count);

        if (categoryRule == null) return false;

        add(categoryRule);

        return true;
    }

    public int update(int id, int templateId, int count) {
        return new CategoryRuleController().update(id, templateId, count);
    }

    public int delete(int id) {
        return new CategoryRuleController().delete(id);
    }
}
