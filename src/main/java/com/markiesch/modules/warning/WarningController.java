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
                uuidFromBytes(resultSet.getBytes("victim")),
                uuidFromBytes(resultSet.getBytes("issuer"))
        );
    }

    public @Nullable WarningModel create(int categoryId, UUID victim, UUID issuer) {
        Object[] parameters = {
                categoryId,
                uuidToBytes(victim),
                uuidToBytes(issuer),
        };
        executeUpdate("INSERT INTO Warning (category_id, victim, issuer)" +
                "VALUES (?, ?, ?)", parameters);

        return new WarningModel(getLastInsertedId("Warning"), categoryId, victim, issuer);
    }

    public List<WarningModel> readAll() {
        return executeRead("SELECT * FROM Warning;", null);
    }

    public int delete(int id) {
        Object[] parameters = { id };
        return executeUpdate("DELETE FROM Warning WHERE id = ?", parameters);
    }
}
