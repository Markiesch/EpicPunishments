package com.markiesch.storage;

import com.markiesch.EpicPunishments;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Storage {
    private final EpicPunishments plugin;
    private Connection connection;
    private final File file;

    public Storage(EpicPunishments plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "data.db");
        setup();
    }

    private void setup() {
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

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}