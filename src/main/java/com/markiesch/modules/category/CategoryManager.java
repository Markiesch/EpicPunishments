package com.markiesch.modules.category;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CategoryManager {
    private final List<CategoryModel> categories;

    private CategoryManager() {
        categories = new CategoryController().readAll();
    }

    private static class CategoryManagerHolder {
        public static final CategoryManager INSTANCE = new CategoryManager();
    }

    public static CategoryManager getInstance() {
        return CategoryManagerHolder.INSTANCE;
    }

    public @Nullable CategoryModel getCategoryById(int id) {
        return categories
                .stream()
                .filter((categoryModel) -> categoryModel.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public @Nullable CategoryModel getCategoryByName(String name) {
        return categories
                .stream()
                .filter((categoryModel) -> categoryModel.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public List<String> getCategoryNames() {
        return categories
                .stream()
                .map(CategoryModel::getName)
                .collect(Collectors.toList());
    }

    public List<String> getCategoryNames(String arg) {
        return getCategoryNames()
                .stream()
                .filter((name) -> name.toLowerCase(Locale.ROOT).contains(arg.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
    }

    public boolean create(String name) {
        CategoryModel category = new CategoryController().create(name);

        if (category != null) {
            categories.add(category);
        }

        return category == null;
    }

    public boolean update(int id, String name) {
        CategoryModel categoryModel = getCategoryById(id);
        if (categoryModel == null) return false;

        boolean success = new CategoryController().update(id, name) == 1;

        if (success) {
            categoryModel.setName(name);
        }

        return success;
    }

    public boolean delete(int id) {
        int affectedRows = new CategoryController().delete(id);
        if (affectedRows == 0) return false;

        categories.removeIf(categoryModel -> categoryModel.getId() == id);
        return true;
    }
}
