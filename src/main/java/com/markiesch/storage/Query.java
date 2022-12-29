package com.markiesch.storage;

import org.intellij.lang.annotations.Language;

public enum Query {
    CREATE_INFRACTION_TABLE(
            "CREATE TABLE IF NOT EXISTS Infraction (" +
                "[id]           INTEGER                 PRIMARY KEY        AUTOINCREMENT," +
                "[victim]       BINARY(16)             NOT NULL," +
                "[issuer]       BINARY(16)," +
                "[type]         VARCHAR(10)," +
                "[reason]       VARCHAR(100)," +
                "[duration]     INTEGER," +
                "[date]         DATE                    NOT NULL," +
                "[revoked]      NUMBER(1)               NOT NULL" +
            ");"
    ),
    CREATE_PLAYER_TABLE(
            "CREATE TABLE IF NOT EXISTS Profile (" +
                "[UUID]         BINARY(16)     NOT NULL    PRIMARY KEY," +
                "[ip]           VARCHAR(39)     NOT NULL," +
                "[name]         VARCHAR(16)     NOT NULL" +
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
    );

    private final String query;

    Query(@Language("SQLite") String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
