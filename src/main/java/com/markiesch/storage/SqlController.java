package com.markiesch.storage;

import org.bukkit.Bukkit;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class SqlController<T> {

    protected abstract T resultSetToModel(ResultSet resultSet) throws SQLException;

    protected List<T> executeRead(@Language("SQLite") String query, @Nullable Object[] parameters) {
        List<T> result = new ArrayList<>();

        Storage storage = Storage.getInstance();

        try {
            Connection connection = storage.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            if (parameters != null) {
                addParameters(preparedStatement, parameters);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                T model = resultSetToModel(resultSet);
                result.add(model);
            }
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to read from database");
        }

        return result;
    }

    protected int executeUpdate(@Language("SQLite") String query, Object[] parameters) {
        Storage storage = Storage.getInstance();

        try {
            Connection connection = storage.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            addParameters(preparedStatement, parameters);

            return preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to write to database");
        }

        return 0;
    }

    protected void executeUpdateBatch(@Language("SQLite") String query, Object[][] batches) {
        Storage storage = Storage.getInstance();

        try {
            Connection connection = storage.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            for (Object[] parameters : batches) {
                addParameters(preparedStatement, parameters);
            }

            preparedStatement.executeBatch();
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to write to database.\n" + sqlException.getMessage());
        }
    }

    private void addParameters(PreparedStatement preparedStatement, Object[] parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            preparedStatement.setObject(i + 1, parameters[i]);
        }
        preparedStatement.addBatch();
    }
}
