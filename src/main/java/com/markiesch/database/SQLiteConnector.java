package com.markiesch.database;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnector extends DatabaseConnector {
    private final File file;

    public SQLiteConnector(Plugin plugin) {
        super(plugin);
        this.file = new File(plugin.getDataFolder(), "data.db");
    }

    @Override
    protected Connection openConnection() throws SQLException {
        try {
            if (!file.exists()) file.createNewFile();
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }

        return DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
    }
}
