package com.markiesch.database;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnector extends DatabaseConnector {
    public MySqlConnector(Plugin plugin) {
        super(plugin);
    }

    @Override
    protected Connection openConnection() throws SQLException {
        FileConfiguration config = plugin.getConfig();

        String hostname = config.getString("my_sql.hostname");
        int port = config.getInt("my_sql.port");
        String database = config.getString("my_sql.database");
        String username = config.getString("my_sql.username");
        String password = config.getString("my_sql.password");

        return DriverManager.getConnection(
                "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false",
                username,
                password
        );
    }
}
