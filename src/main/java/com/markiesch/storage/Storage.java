package com.markiesch.storage;

import com.markiesch.EpicPunishments;
import com.markiesch.database.DatabaseConnector;
import com.markiesch.database.MySqlConnector;
import com.markiesch.database.SQLiteConnector;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

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

    public void init(EpicPunishments plugin) throws SQLException {
        boolean mySqlEnabled = plugin.getConfig().getBoolean("my_sql.enabled");
        databaseConnector = mySqlEnabled ? new MySqlConnector(plugin) : new SQLiteConnector(plugin);

        databaseConnector.openConnection();
    }

    public void createDatabaseTables(Plugin plugin) {
        boolean mySqlEnabled = plugin.getConfig().getBoolean("my_sql.enabled");
        String autoIncrement = mySqlEnabled ? "AUTO_INCREMENT" : "AUTOINCREMENT";

        plugin.getLogger().info("Creating SQLite tables...");
        String profileTableQuery =
                "CREATE TABLE IF NOT EXISTS Profile (" +
                        "UUID         BINARY(16)        NOT NULL    PRIMARY KEY," +
                        "ip           VARCHAR(39)       NOT NULL," +
                        "name         VARCHAR(16)       NOT NULL" +
                        ");";

        String templateTableQuery =
                "CREATE TABLE IF NOT EXISTS Template (" +
                        "id           INTEGER           PRIMARY KEY " + autoIncrement + "," +
                        "name         VARCHAR(50)       NOT NULL," +
                        "type         VARCHAR(10)       NOT NULL," +
                        "reason       VARCHAR(255)," +
                        "duration     INTEGER" +
                        ");";

        String infractionTableQuery =
                "CREATE TABLE IF NOT EXISTS Infraction (" +
                        "id           INTEGER           PRIMARY KEY " + autoIncrement + "," +
                        "victim       BINARY(16)        NOT NULL," +
                        "issuer       BINARY(16)," +
                        "type         VARCHAR(10)," +
                        "reason       VARCHAR(255)," +
                        "duration     INTEGER," +
                        "date         INTEGER           NOT NULL," +
                        "revoked      BIT(1)            NOT NULL" +
                        ");";

        String warnTableQuery =
                "CREATE TABLE IF NOT EXISTS Warning (" +
                        "id                 INTEGER             PRIMARY KEY " + autoIncrement + "," +
                        "category_id        INTEGER," +
                        "reason             VARCHAR(255)," +
                        "victim             BINARY(16)          NOT NULL," +
                        "issuer             BINARY(16)," +
                        "created            INTEGER             NOT NULL," +
                        "seen               BIT(1)" +
                        ");";

        String categoryTableQuery =
                "CREATE TABLE IF NOT EXISTS Category (" +
                        "id             INTEGER             PRIMARY KEY " + autoIncrement + "," +
                        "name           VARCHAR(40)         NOT NULL    UNIQUE," +
                        "message        TEXT" +
                        ");";

        String categoryPunishmentTableQuery =
                "CREATE TABLE IF NOT EXISTS CategoryRule (" +
                        "id                     INTEGER          PRIMARY KEY " + autoIncrement + "," +
                        "category_id            INTEGER          NOT NULL," +
                        "template_id            INTEGER          NOT NULL," +
                        "count                  INTEGER          NOT NULL" +
                        ");";


        try (Connection connection = getConnection()) {
            connection.prepareStatement(profileTableQuery).executeUpdate();
            connection.prepareStatement(templateTableQuery).executeUpdate();
            connection.prepareStatement(infractionTableQuery).executeUpdate();
            connection.prepareStatement(warnTableQuery).executeUpdate();
            connection.prepareStatement(categoryTableQuery).executeUpdate();
            connection.prepareStatement(categoryPunishmentTableQuery).executeUpdate();
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to execute database query!");
            Bukkit.getLogger().warning(sqlException.getMessage());
        }

        plugin.getLogger().info("Created SQLite tables!");
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