package com.markiesch.controllers;

import com.markiesch.EpicPunishments;
import com.markiesch.models.InfractionModel;
import com.markiesch.storage.Query;
import com.markiesch.storage.Storage;
import com.markiesch.utils.PunishTypes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class InfractionController {
    private final Storage storage;

    public InfractionController(EpicPunishments plugin) {
        storage = plugin.getStorage();
    }

    public void create(PunishTypes type, UUID victim, UUID issuer, String reason, long duration, long date) {
        try {
            PreparedStatement preparedStatement = storage.getConnection().prepareStatement(
                    "REPLACE INTO Infraction (Victim, Issuer, Type, Reason, Duration, Date)" +
                            "VALUES (?, ?, ?, ?, ?, ?);"
            );

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
        List<InfractionModel> result = new ArrayList<>();

        try {
            Connection connection = storage.getConnection();

            PreparedStatement statement = connection.prepareStatement(Query.SELECT_INFRACTIONS.getQuery());
            statement.setString(1, uuid.toString());

            statement.executeQuery();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            storage.closeConnection();
        }

        return result;
    }
}
