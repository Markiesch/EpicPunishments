package com.markiesch.modules.infraction;

import com.markiesch.storage.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class InfractionController {
    private final Storage storage;

    public InfractionController() {
        storage = Storage.getInstance();
    }

    public void create(InfractionType type, UUID victim, UUID issuer, String reason, long duration) {
        try {
            PreparedStatement preparedStatement = storage.getConnection().prepareStatement(
                    "REPLACE INTO Infraction (Victim, Issuer, Type, Reason, Duration, Date)" +
                            "VALUES (?, ?, ?, ?, ?, ?);"
            );

            long date = System.currentTimeMillis() / 1000L;

            // Insert model values
            preparedStatement.setString(1, victim.toString());
            preparedStatement.setString(2, issuer.toString());
            preparedStatement.setString(3, type.toString());
            preparedStatement.setString(4, reason);
            preparedStatement.setLong(5, duration);
            preparedStatement.setLong(6, date);
            preparedStatement.executeUpdate();

            storage.closeConnection();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public List<InfractionModel> readAll(UUID uuid) {
        List<InfractionModel> infractions = new ArrayList<>();

        try {
            Connection connection = storage.getConnection();

            PreparedStatement statement = connection.prepareStatement(InfractionQuery.SELECT_INFRACTIONS);
            statement.setString(1, uuid.toString());

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                int id = result.getInt("id");
                UUID victim = UUID.fromString(result.getString("victim"));
                UUID issuer = UUID.fromString(result.getString("issuer"));
                InfractionType type = InfractionType.valueOf(result.getString("type"));
                String reason = result.getString("reason");
                int duration = result.getInt("duration");
                int date = result.getInt("date");
                infractions.add(new InfractionModel(id, type, victim, issuer, reason, duration, date));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
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
            sqlException.printStackTrace();
        } finally {
            storage.closeConnection();
        }
    }
}
