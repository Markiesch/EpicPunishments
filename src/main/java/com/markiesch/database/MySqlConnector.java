package com.markiesch.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.SQLException;

public class MySqlConnector implements DatabaseConnector {
    private final Plugin plugin;
    private HikariDataSource hikari;

    public MySqlConnector(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void openConnection() {
        FileConfiguration pluginConfig = plugin.getConfig();

        String hostname = pluginConfig.getString("my_sql.hostname");
        int port = pluginConfig.getInt("my_sql.port");
        String database = pluginConfig.getString("my_sql.database");
        String username = pluginConfig.getString("my_sql.username");
        String password = pluginConfig.getString("my_sql.password");
        boolean useSSL = pluginConfig.getBoolean("my_sql.useSSL");
        int poolSize = pluginConfig.getInt("my_sql.pool_size");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=" + useSSL);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(poolSize);
        hikari = new HikariDataSource(config);
    }

    @Override
    public Connection getConnection() {
        try {
            if (hikari == null) openConnection();
            return hikari.getConnection();
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning(sqlException.getMessage());
            return null;
        }
    }

    @Override
    public void closeConnection() {
        if (hikari == null || hikari.isClosed()) return;
        hikari.close();
    }
}
