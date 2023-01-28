package com.markiesch.modules.template;

import com.markiesch.database.SqlController;
import com.markiesch.modules.infraction.InfractionType;
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

    public @Nullable TemplateModel create(String name, String reason, InfractionType type, Long duration) {
        Object[] parameters = {name, type.name(), reason, duration};
        int affectedRows = executeUpdate("INSERT INTO Template (name, type, reason, duration) VALUES(?, ?, ?, ?)", parameters);

        if (affectedRows == 0) return null;

        return new TemplateModel(getLastInsertedId("Template"), name, reason, type, duration);
    }

    public int delete(Integer id) {
        Object[] parameters = {id};
        return executeUpdate("DELETE FROM Template WHERE id = ?;", parameters);
    }

    public int update(int id, String name, InfractionType type, String reason, long duration) {
        Object[] parameters = {name, type.name(), reason, duration, id};
        return executeUpdate("UPDATE Template " +
                "SET name = ?, type = ?, reason = ?, duration = ? " +
                "WHERE id = ?;",
                parameters
        );
    }
}
