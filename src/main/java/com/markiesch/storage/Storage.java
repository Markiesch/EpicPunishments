package com.markiesch.storage;

import com.markiesch.EpicPunishments;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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

            // Create tables
            plugin.getLogger().info("Creating SQLite tables...");
            executeUpdate(Query.CREATE_INFRACTION_TABLE);
            executeUpdate(Query.CREATE_PLAYER_TABLE);
            executeUpdate(Query.CREATE_TEMPLATE_TABLE);
            plugin.getLogger().info("Created SQLite tables!");
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to initialize SQL tables" + sqlException.getMessage());
        } finally {
            closeConnection();
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
        try (Statement statement = getConnection().createStatement()) {
            statement.execute("SELECT last_insert_rowid()");
            return statement.getUpdateCount();
        } catch (SQLException exception) {
            Bukkit.getLogger().warning("Failed to retrieve last inserted SQL ID");
        }

        return null;
    }
}