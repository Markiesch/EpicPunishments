package com.markiesch.database;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DatabaseConnector {
    protected final Plugin plugin;
    private Connection connection;

    public DatabaseConnector(Plugin plugin) {
        this.plugin = plugin;
    }

    protected abstract Connection openConnection() throws SQLException;

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = openConnection();
            }
        } catch (SQLException exception) {
            Bukkit.getLogger().warning(exception.getMessage());
        }

        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to close SQL connection");
        }
    }
}
