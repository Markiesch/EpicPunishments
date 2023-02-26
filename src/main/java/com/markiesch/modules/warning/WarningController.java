package com.markiesch.modules.warning;

import com.markiesch.database.SqlController;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class WarningController extends SqlController<WarningModel> {
    @Override
    protected WarningModel resultSetToModel(ResultSet resultSet) throws SQLException {
        return new WarningModel(
            resultSet.getInt("id"),
            resultSet.getInt("category_id"),
            resultSet.getString("reason"),
            uuidFromBytes(resultSet.getBytes("victim")),
            uuidFromBytes(resultSet.getBytes("issuer")),
            resultSet.getBoolean("seen")
        );
    }

    public @Nullable WarningModel create(@Nullable Integer categoryId, @Nullable String reason, UUID victim, UUID issuer, boolean seen) {
        Object[] parameters = {
            categoryId,
            reason,
            uuidToBytes(victim),
            uuidToBytes(issuer),
            System.currentTimeMillis() / 1000L,
            seen,
        };
        executeUpdate("INSERT INTO Warning (category_id, reason, victim, issuer, created, seen)" +
            "VALUES (?, ?, ?, ?, ?, ?)", parameters);

        return new WarningModel(
            getLastInsertedId("Warning"),
            categoryId,
            reason,
            victim,
            issuer,
            seen
        );
    }

    public List<WarningModel> readAll() {
        return executeRead("SELECT * FROM Warning;", null);
    }

    public int update(int warningId, boolean seen) {
        Object[] parameters = { seen, warningId };
        return executeUpdate("UPDATE Warning SET seen = ? WHERE id = ?;",
            parameters
        );
    }

    public int delete(int id) {
        Object[] parameters = { id };
        return executeUpdate("DELETE FROM Warning WHERE id = ?", parameters);
    }

}
