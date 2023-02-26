package com.markiesch.modules.category;

import org.jetbrains.annotations.Nullable;

public class CategoryModel {
    private final int id;
    private String name;
    private @Nullable String message;

    public CategoryModel(int id, String name, @Nullable String message) {
        this.id = id;
        this.name = name;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @Nullable String getMessage() {
        return message;
    }

    public void setMessage(@Nullable String message) {
        this.message = message;
    }
}
