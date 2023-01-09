package com.markiesch.modules.infraction;

import com.markiesch.database.SqlController;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class InfractionController extends SqlController<InfractionModel> {
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
        executeUpdate("INSERT INTO Infraction (Infraction.Victim, Infraction.Issuer, Infraction.Type, Infraction.Reason, Infraction.Duration, Infraction.Date, Infraction.revoked)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)", parameters);

        return preparedInfraction.createInfraction(getLastInsertedId());
    }

    public InfractionList readAll(UUID uuid) {
        Object[] parameters = {uuid};
        return new InfractionList(executeRead("SELECT * FROM Infraction WHERE Infraction.victim = ?;", parameters));
    }

    public InfractionList readAll() {
        return new InfractionList(executeRead("SELECT * FROM Infraction;", null));
    }

    public boolean delete(Integer id) {
        Object[] parameters = {id};
        int affectedRows = executeUpdate("DELETE FROM Infraction WHERE Infraction.id = ?;", parameters);
        return affectedRows == 1;
    }

    public void expire(InfractionList infractionList) {
        Integer[] ids = infractionList.stream()
                .map(infractionModel -> infractionModel.id)
                .toArray(Integer[]::new);

        @Language("MariaDB") String query = String.format("UPDATE Infraction SET Infraction.revoked = 1 WHERE Infraction.id in (%s);",
                Arrays.stream(ids)
                        .map(id -> "?")
                        .collect(Collectors.joining(", "))
        );

        executeUpdate(query, ids);
    }
}
