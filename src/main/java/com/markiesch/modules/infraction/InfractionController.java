package com.markiesch.modules.infraction;

import com.markiesch.storage.SqlController;
import com.markiesch.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
                UUID.fromString(resultSet.getString("victim")),
                resultSet.getString("issuer") == null ? null : UUID.fromString(resultSet.getString("issuer")),
                resultSet.getString("reason"),
                resultSet.getInt("duration"),
                resultSet.getInt("date"),
                resultSet.getBoolean("revoked")
        );
    }

    public @Nullable InfractionModel create(PreparedInfraction preparedInfraction) {
        Object[] parameters = {
                preparedInfraction.victim.getUniqueId().toString(),
                preparedInfraction.issuer == null ? null : ((Player) preparedInfraction.issuer).getUniqueId().toString(),
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
        try {
            Connection connection = storage.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Infraction SET revoked = 1 WHERE id = ?");

            List<Integer> keys = infractionList.stream().map(infractionModel -> infractionModel.id).collect(Collectors.toList());
            for (Integer key : keys) {
                preparedStatement.setInt(1, key);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to write to database");
        }
    }
}
