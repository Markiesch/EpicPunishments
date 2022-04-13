package com.markiesch.storage;

public enum Query {
    CREATE_INFRACTION_TABLE(
            "CREATE TABLE IF NOT EXISTS Infraction (" +
                "[id]           INTEGER                 PRIMARY KEY        AUTOINCREMENT," +
                "[victim]       VARCHAR(36)             NOT NULL," +
                "[issuer]       VARCHAR(36)," +
                "[type]         VARCHAR(10)," +
                "[reason]       VARCHAR(100)," +
                "[duration]     INTEGER," +
                "[date]         DATE                    NOT NULL" +
            ");"
    ),
    CREATE_PLAYER_TABLE(
            "CREATE TABLE IF NOT EXISTS Profile (" +
                "[UUID]         VARCHAR(36)     PRIMARY KEY," +
                "[ipHistory]    LONGTEXT" +
            ");"
    ),
    CREATE_TEMPLATE_TABLE(
            "CREATE TABLE IF NOT EXISTS Template (" +
                "[id]           INTEGER             PRIMARY KEY     AUTOINCREMENT," +
                "[name]         VARCHAR(50)         NOT NULL," +
                "[type]         VARCHAR(10)         NOT NULL," +
                "[reason]       VARCHAR(100)," +
                "[duration]     INTEGER" +
            ");"
    ),
    ADD_PROFILE(
            "INSERT OR REPLACE INTO Profile (UUID, ipHistory)" +
            "VALUES(?, ?) " +
            "ON CONFLICT(UUID) DO UPDATE SET " +
            "ipHistory = ipHistory || ';' || ?"
    ),
//            "INSERT OR REPLACE INTO Profile (UUID, ipHistory) " +
//            "VALUES(?, CONCAT(Profile.ipHistory, ?));"
    SELECT_PROFILE("SELECT * FROM Profile WHERE UUID = ?;"),
    SELECT_PROFILES("SELECT * FROM Profile;"),
    SELECT_INFRACTION("SELECT * FROM Infraction WHERE [id] = ?;"),
    SELECT_INFRACTIONS("SELECT * FROM Infraction WHERE victim = ?;");

    private final String query;

    Query(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
