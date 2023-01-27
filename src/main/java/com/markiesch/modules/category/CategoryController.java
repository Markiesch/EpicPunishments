package com.markiesch.modules.category;

import com.markiesch.database.SqlController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CategoryController extends SqlController<CategoryModel> {
    @Override
    protected CategoryModel resultSetToModel(ResultSet resultSet) throws SQLException {
        return new CategoryModel(
                resultSet.getInt("id"),
                resultSet.getString("category_name")
        );
    }

    public List<CategoryModel> readAll() {
        return executeRead("SELECT * FROM Category;", null);
    }

    public int update(int id, String name) {
        return executeUpdate("UPDATE Category SET category_name = ? WHERE id = ?;", new Object[]{name, id});
    }

    public CategoryModel create(String name) {
        int affectedRows = executeUpdate("INSERT INTO Category(category_name) VALUES (?);", new Object[]{name});
        if (affectedRows == 0) return null;

        return new CategoryModel(getLastInsertedId("Category"), name);
    }

    public int delete(int id) {
        return  executeUpdate("DELETE FROM Category WHERE id = ?;", new Object[]{id});
    }
}
