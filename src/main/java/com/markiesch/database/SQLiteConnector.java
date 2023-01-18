package com.markiesch.database;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnector implements DatabaseConnector {
    private final File file;
    private Connection connection;

    public SQLiteConnector(Plugin plugin) {
        this.file = new File(plugin.getDataFolder(), "data.db");
    }

    @Override
    public void openConnection() throws SQLException {
        try {
            if (!file.exists()) file.createNewFile();
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }

        connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) openConnection();
        } catch (SQLException exception) {
            Bukkit.getLogger().warning(exception.getMessage());
        }

        return connection;
    }

    @Override
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to close SQL connection");
        }
    }
}
