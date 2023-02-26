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
                resultSet.getString("name"),
                resultSet.getString("message")
        );
    }

    public List<CategoryModel> readAll() {
        return executeRead("SELECT * FROM Category;", null);
    }

    public int update(int id, String name, String message) {
        return executeUpdate("UPDATE Category SET name = ?, message = ? WHERE id = ?;", new Object[]{name, message, id});
    }

    public CategoryModel create(String name, String message) {
        int affectedRows = executeUpdate("INSERT INTO Category(name, message) VALUES (?, ?);", new Object[]{name});
        if (affectedRows == 0) return null;

        return new CategoryModel(getLastInsertedId("Category"), name, message);
    }

    public int delete(int id) {
        return  executeUpdate("DELETE FROM Category WHERE id = ?;", new Object[]{id});
    }
}
