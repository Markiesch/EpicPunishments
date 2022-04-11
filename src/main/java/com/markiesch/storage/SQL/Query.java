package com.markiesch.storage.SQL;

public enum Query {
    CREATE_INFRACTION_TABLE(
            "CREATE TABLE IF NOT EXISTS Infraction (" +
                "[id]         INT                 PRIMARY KEY        AUTOINCREMENT," +
                "[victim]     VARCHAR(36)         NOT NULL," +
                "[issuer]     VARCHAR(36)," +
                "[type]       VARCHAR(10)," +
                "[reason]     VARCHAR(100)," +
                "[duration]   INT," +
                "[date]       DATE                NOT NULL" +
            ");"
    ),
    CREATE_PLAYER_TABLE(
            "CREATE TABLE IF NOT EXISTS Player (" +
                "[UUID]       VARCHAR(36)     PRIMARY KEY," +
                "[ipHistory]  LONGTEXT" +
            ");"
    ),
    CREATE_TEMPLATE_TABLE(
            "CREATE TABLE IF NOT EXISTS Template (" +
                "[id]           INT                 PRIMARY KEY     AUTOINCREMENT," +
                "[name]         VARCHAR(50)         NOT NULL," +
                "[type]         VARCHAR(10)         NOT NULL," +
                "[reason]       VARCHAR(100)," +
                "[duration]     INT" +
            ");"
    ),
    ADD_PROFILE("INSERT INTO Player (UUID) VALUES(?)"),
    SELECT_PROFILE("SELECT * FROM Player WHERE UUID = ?"),
    SELECT_PROFILES("SELECT * FROM Player");

    private final String query;

    Query(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
