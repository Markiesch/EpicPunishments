package com.markiesch.modules.infraction;

import com.markiesch.storage.SqlController;
import com.markiesch.storage.Storage;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class InfractionController extends SqlController<InfractionModel> {
    private final Storage storage;

    public InfractionController() {
        storage = Storage.getInstance();
    }

    @Override
    protected InfractionModel resultSetToModel(ResultSet resultSet) throws SQLException {
        return new InfractionModel(
                resultSet.getInt("id"),
                InfractionType.valueOf(resultSet.getString("type")),
                uuidFromBytes(resultSet.getBytes("victim")),
                resultSet.getBytes("issuer") == null ? null : uuidFromBytes(resultSet.getBytes("issuer")),
                resultSet.getString("reason"),
                resultSet.getInt("duration"),
                resultSet.getInt("date"),
                resultSet.getBoolean("revoked")
        );
    }

    public @Nullable InfractionModel create(PreparedInfraction preparedInfraction) {
        Object[] parameters = {
                uuidToBytes(preparedInfraction.victimProfile.uuid),
                preparedInfraction.getIssuerUUID() == null ? null : uuidToBytes(preparedInfraction.getIssuerUUID()),
                preparedInfraction.type.name(),
                preparedInfraction.reason,
                preparedInfraction.duration,
                preparedInfraction.date,
                0
        };
        executeUpdate("REPLACE INTO Infraction (Victim, Issuer, Type, Reason, Duration, Date, revoked) VALUES (?, ?, ?, ?, ?, ?, ?);", parameters);

        return preparedInfraction.createInfraction(storage.getLastInsertedId());
    }

    public InfractionList readAll(UUID uuid) {
        Object[] parameters = {uuid};

        return new InfractionList(executeRead("SELECT * FROM Infraction WHERE victim = ?;", parameters));
    }

    public InfractionList readAll() {
        return new InfractionList(executeRead("SELECT * FROM Infraction;", null));
    }

    public boolean delete(Integer id) {
        Object[] parameters = {id};

        int affectedRows = executeUpdate("DELETE FROM Infraction WHERE [id] = ?; SELECT changes();", parameters);

        return affectedRows == 1;
    }

    public void expire(InfractionList infractionList) {
        Integer[][] ids = infractionList.stream()
                .map(infractionModel -> infractionModel.id)
                .map(id -> new Integer[]{id})
                .toArray(Integer[][]::new);

        executeUpdateBatch("UPDATE Infraction SET revoked = 1 WHERE id = ?;", ids);
    }
}
