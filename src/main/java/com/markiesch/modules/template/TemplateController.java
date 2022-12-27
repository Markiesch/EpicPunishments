package com.markiesch.modules.template;

import com.markiesch.modules.infraction.InfractionType;
import com.markiesch.storage.SqlController;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TemplateController extends SqlController<TemplateModel> {
    @Override
    protected TemplateModel resultSetToModel(ResultSet resultSet) throws SQLException {
        return new TemplateModel(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("reason"),
                InfractionType.valueOf(resultSet.getString("type")),
                resultSet.getLong("duration")
        );
    }

    public List<TemplateModel> readAll() {
        return executeRead("SELECT * FROM Template", null);
    }

    public @Nullable TemplateModel readSingle(int id) {
        return executeRead(TemplateQuery.SELECT_SINGLE_TEMPLATE, new Object[]{id})
                .stream()
                .findFirst()
                .orElse(null);
    }

    public void create(String name, String reason, InfractionType type, Long duration) {
        executeUpdate(TemplateQuery.CREATE_TEMPLATE, new Object[]{name, type.name(), reason, duration});
    }

    public void delete(Integer id) {
        executeUpdate(TemplateQuery.DELETE_TEMPLATE, new Object[]{id});
    }

    public void update(int id, String name, InfractionType type, String reason, long duration) {
        executeUpdate(TemplateQuery.UPDATE_TEMPLATE, new Object[]{name, type.name(), reason, duration, id});
    }
}
