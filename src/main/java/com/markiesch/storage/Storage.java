package com.markiesch.storage;

import com.markiesch.EpicPunishments;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
            System.out.println("Creating SQLite tables...");
            executeUpdate(Query.CREATE_INFRACTION_TABLE);
            executeUpdate(Query.CREATE_PLAYER_TABLE);
            executeUpdate(Query.CREATE_TEMPLATE_TABLE);
            System.out.println("Created SQLite tables!");

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
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
            sqlException.printStackTrace();
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
            sqlException.printStackTrace();
        }
    }
}