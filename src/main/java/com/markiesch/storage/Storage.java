package com.markiesch.storage;

import com.markiesch.EpicPunishments;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class Storage {
    private Connection connection;
    private File file;

    private Storage() {}

    private static class StorageHolder {
        static final Storage INSTANCE = new Storage();
    }

    public static Storage getInstance() {
        return StorageHolder.INSTANCE;
    }

    public void setup(EpicPunishments plugin) {
        this.file = new File(plugin.getDataFolder(), "data.db");

        try {
            connection = getConnection();
            connection.setAutoCommit(true);

            // Create tables
            plugin.getLogger().info("Creating SQLite tables...");
            executeUpdate(Query.CREATE_INFRACTION_TABLE);
            executeUpdate(Query.CREATE_PLAYER_TABLE);
            executeUpdate(Query.CREATE_TEMPLATE_TABLE);
            plugin.getLogger().info("Created SQLite tables!");
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to initialize SQL tables" + sqlException.getMessage());
        }
    }

    /**
     * Executes an update query to the (local) database
     * @param query The query that needs to be executed
     */
    private void executeUpdate(Query query) {
        try {
            Connection connection = getConnection();
            connection.prepareStatement(query.getQuery()).executeUpdate();
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to execute database query!");
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) openConnection();
        return connection;
    }

    /**
     * Creates a new connection to the local SQLite database
     */
    private void openConnection() {
        try {
            // Create file if it does not exist
            if (!file.exists()) file.createNewFile();

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
        } catch (SQLException | IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Closes the connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to close SQL connection");
        }
    }

    public Integer getLastInsertedId() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT last_insert_rowid();");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                Bukkit.getLogger().warning("Failed to retrieve last inserted SQL ID");
                return null;
            }
            return resultSet.getInt("last_insert_rowid()");
        } catch (SQLException exception) {
            Bukkit.getLogger().warning("Failed to retrieve last inserted SQL ID");
        }

        return null;
    }
}