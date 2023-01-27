package com.markiesch.modules.categoryRule;

import com.markiesch.database.SqlController;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CategoryRuleController extends SqlController<CategoryRuleModel> {
    @Override
    protected CategoryRuleModel resultSetToModel(ResultSet resultSet) throws SQLException {
        return new CategoryRuleModel(
                resultSet.getInt("id"),
                resultSet.getInt("category_id"),
                resultSet.getInt("template_id"),
                resultSet.getInt("count")
        );
    }

    public @Nullable CategoryRuleModel create(int categoryId, int templateId, int count) {
        int affectedRows = executeUpdate(
                "INSERT INTO CategoryRule(category_id, template_id, count) VALUES (?, ?, ?);",
                new Object[]{ categoryId, templateId, count }
        );

        if (affectedRows == 0) return null;

        return new CategoryRuleModel(getLastInsertedId("CategoryRule"), categoryId, templateId, count);
    }

    public List<CategoryRuleModel> readAll() {
        return executeRead("SELECT * FROM CategoryRule", null);
    }

    public int update(int id, int templateId, int count) {
        return executeUpdate(
                "UPDATE CategoryRule set template_id = ?, count = ? WHERE id = ?;",
                new Object[]{ templateId, count, id }
        );
    }

    public int delete(int id) {
        return executeUpdate("DELETE FROM CategoryRule WHERE id = ?", new Object[]{ id });
    }
}
