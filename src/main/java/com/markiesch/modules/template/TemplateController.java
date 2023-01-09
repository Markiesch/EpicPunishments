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

    public @Nullable TemplateModel readSingle(int id) {
        return executeRead("SELECT * FROM Template WHERE Template.id = ?", new Object[]{id})
                .stream()
                .findFirst()
                .orElse(null);
    }

    public void create(String name, String reason, InfractionType type, Long duration) {
        Object[] parameters = {name, type.name(), reason, duration};
        executeUpdate("INSERT INTO Template (Template.name, Template.type, Template.reason, Template.duration) VALUES(?, ?, ?, ?)", parameters);
    }

    public void delete(Integer id) {
        Object[] parameters = {id};
        executeUpdate("DELETE FROM Template WHERE Template.id = ?;", parameters);
    }

    public void update(int id, String name, InfractionType type, String reason, long duration) {
        Object[] parameters = {name, type.name(), reason, duration, id};
        executeUpdate("UPDATE Template " +
                "SET Template.name = ?, Template.type = ?, Template.reason = ?, Template.duration = ? " +
                "WHERE Template.id = ?;",
                parameters
        );
    }
}
