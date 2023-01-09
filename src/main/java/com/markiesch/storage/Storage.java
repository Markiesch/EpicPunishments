package com.markiesch.storage;

import com.markiesch.EpicPunishments;
import com.markiesch.database.DatabaseConnector;
import com.markiesch.database.MySqlConnector;
import com.markiesch.database.SQLiteConnector;
import org.bukkit.Bukkit;
import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.SQLException;

public class Storage {
    private DatabaseConnector databaseConnector;

    private Storage() {
    }

    private static class StorageHolder {
        static final Storage INSTANCE = new Storage();
    }

    public static Storage getInstance() {
        return StorageHolder.INSTANCE;
    }

    public void init(EpicPunishments plugin) {
        boolean mySqlEnabled = plugin.getConfig().getBoolean("my_sql.enabled");
        databaseConnector = mySqlEnabled ? new MySqlConnector(plugin) : new SQLiteConnector(plugin);

        // Create tables
        String autoIncrement = mySqlEnabled ? "AUTO_INCREMENT" : "AUTOINCREMENT";

        plugin.getLogger().info("Creating SQLite tables...");
        executeUpdate("CREATE TABLE IF NOT EXISTS Infraction (" +
                "id           INTEGER                 PRIMARY KEY " + autoIncrement + "," +
                "victim       BINARY(16)              NOT NULL," +
                "issuer       BINARY(16)," +
                "type         VARCHAR(10)," +
                "reason       VARCHAR(100)," +
                "duration     INTEGER," +
                "date         INTEGER                 NOT NULL," +
                "revoked      BIT(1)                  NOT NULL" +
                ");"
        );
        executeUpdate("CREATE TABLE IF NOT EXISTS Profile (" +
                "UUID         BINARY(16)     NOT NULL    PRIMARY KEY," +
                "ip           VARCHAR(39)     NOT NULL," +
                "name         VARCHAR(16)     NOT NULL" +
                ");"
        );
        executeUpdate("CREATE TABLE IF NOT EXISTS Template (" +
                "id           INTEGER             PRIMARY KEY " + autoIncrement + "," +
                "name         VARCHAR(50)         NOT NULL," +
                "type         VARCHAR(10)         NOT NULL," +
                "reason       VARCHAR(100)," +
                "duration     INTEGER" +
                ");"
        );
        plugin.getLogger().info("Created SQLite tables!");
    }

    /**
     * Executes an update query to the (local) database
     *
     * @param query The query that needs to be executed
     */
    private void executeUpdate(@Language("MariaDB") String query) {
        try {
            Connection connection = getConnection();
            connection.prepareStatement(query).executeUpdate();
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to execute database query!");
            Bukkit.getLogger().warning(sqlException.getMessage());
        }
    }

    public Connection getConnection() {
        return databaseConnector.getConnection();
    }

    /**
     * Closes the connection
     */
    public void closeConnection() {
        databaseConnector.closeConnection();
    }
}