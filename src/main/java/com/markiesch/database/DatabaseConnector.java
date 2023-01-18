package com.markiesch.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnector {
    void openConnection() throws SQLException;
    Connection getConnection();
    void closeConnection();
}
