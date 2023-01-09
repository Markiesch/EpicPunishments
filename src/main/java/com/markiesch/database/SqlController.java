package com.markiesch.database;

import com.markiesch.EpicPunishments;
import com.markiesch.storage.Storage;
import org.bukkit.Bukkit;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class SqlController<T> {

    protected abstract T resultSetToModel(ResultSet resultSet) throws SQLException;

    protected List<T> executeRead(@Language("SQLite") String query, @Nullable Object[] parameters) {
        List<T> result = new ArrayList<>();

        Storage storage = Storage.getInstance();

        try (PreparedStatement preparedStatement = storage.getConnection().prepareStatement(query)) {
            if (parameters != null) {
                addParameters(preparedStatement, parameters);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    T model = resultSetToModel(resultSet);
                    result.add(model);
                }
            }
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to read from database");
        }

        return result;
    }

    protected int executeUpdate(@Language("SQL") String query, Object[] parameters) {
        Storage storage = Storage.getInstance();

        try (PreparedStatement preparedStatement = storage.getConnection().prepareStatement(query)) {
            addParameters(preparedStatement, parameters);

            preparedStatement.executeUpdate();
            return preparedStatement.getUpdateCount();
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to write to database");
            Bukkit.getLogger().warning(sqlException.getMessage());
        }

        return 0;
    }

    private void addParameters(PreparedStatement preparedStatement, Object[] parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            preparedStatement.setObject(i + 1, parameters[i]);
        }
        preparedStatement.addBatch();
    }

    protected Integer getLastInsertedId() {
        Storage storage = Storage.getInstance();

        String query = EpicPunishments.getInstance().getConfig().getBoolean("my_sql.enabled") ? "LAST_INSERT_ID()" : "last_insert_rowid()";
        try (PreparedStatement preparedStatement = storage.getConnection().prepareStatement("SELECT " + query + ";");
             ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            if (!resultSet.next()) {
                Bukkit.getLogger().warning("Failed to retrieve last inserted SQL ID");
                return null;
            }
            return resultSet.getInt(query);
        } catch (SQLException exception) {
            Bukkit.getLogger().warning("Failed to retrieve last inserted SQL ID");
        }

        return null;
    }

    protected static byte[] uuidToBytes(final @NotNull UUID uuid) {
        return ByteBuffer.allocate(16).putLong(uuid.getMostSignificantBits()).putLong(uuid.getLeastSignificantBits()).array();
    }

    protected static UUID uuidFromBytes(final byte[] bytes) {
        if (bytes.length < 2) {
            throw new IllegalArgumentException("Byte array too small.");
        }
        final ByteBuffer bb = ByteBuffer.wrap(bytes);
        return new UUID(bb.getLong(), bb.getLong());
    }
}
