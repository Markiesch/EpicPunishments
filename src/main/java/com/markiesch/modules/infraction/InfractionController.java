package com.markiesch.modules.infraction;

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

public class InfractionController {
    private final Storage storage;

    public InfractionController() {
        storage = Storage.getInstance();
    }

    public @Nullable InfractionModel create(PreparedInfraction preparedInfraction) {
        InfractionModel infractionModel = null;

        try {
            PreparedStatement preparedStatement = storage.getConnection().prepareStatement(
                    "REPLACE INTO Infraction (Victim, Issuer, Type, Reason, Duration, Date, revoked)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?);"
            );

            // Insert model values
            preparedStatement.setString(1, preparedInfraction.victim.getUniqueId().toString());
            preparedStatement.setString(2, preparedInfraction.issuer == null ? null : ((Player)preparedInfraction.issuer).getUniqueId().toString());
            preparedStatement.setString(3, preparedInfraction.type.name());
            preparedStatement.setString(4, preparedInfraction.reason);
            preparedStatement.setLong(5, preparedInfraction.duration);
            preparedStatement.setLong(6, preparedInfraction.date);
            preparedStatement.setInt(7, 0);
            preparedStatement.executeUpdate();

            infractionModel = preparedInfraction.createInfraction(storage.getLastInsertedId());

            storage.closeConnection();
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to write to database");
        }

        return infractionModel;
    }

    public InfractionList readAll(UUID uuid) {
        InfractionList infractions = new InfractionList();

        try {
            Connection connection = storage.getConnection();

            PreparedStatement statement = connection.prepareStatement(InfractionQuery.SELECT_INFRACTIONS);
            statement.setString(1, uuid.toString());

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                int id = result.getInt("id");
                UUID victim = UUID.fromString(result.getString("victim"));
                UUID issuer = result.getString("issuer") == null ? null : UUID.fromString(result.getString("issuer"));
                InfractionType type = InfractionType.valueOf(result.getString("type"));
                String reason = result.getString("reason");
                int duration = result.getInt("duration");
                int date = result.getInt("date");
                boolean revoked = result.getBoolean("revoked");
                infractions.add(new InfractionModel(id, type, victim, issuer, reason, duration, date, revoked));
            }
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to read from database");
        } finally {
            storage.closeConnection();
        }

        return infractions;
    }

    public void delete(Integer id) {
        try {
            Connection connection = storage.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(InfractionQuery.DELETE_INFRACTION);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to write to database");
        } finally {
            storage.closeConnection();
        }
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
        } finally {
            storage.closeConnection();
        }
    }
}
