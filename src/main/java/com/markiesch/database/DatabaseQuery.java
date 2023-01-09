package com.markiesch.database;

import com.markiesch.EpicPunishments;
import org.intellij.lang.annotations.Language;

public class DatabaseQuery {
    private final String sqLiteQuery;
    private final String mySqlQuery;

    public DatabaseQuery(@Language("SQLite") String sqLiteQuery, @Language("MySQL") String mySqlQuery) {
        this.sqLiteQuery = sqLiteQuery;
        this.mySqlQuery = mySqlQuery;
    }

    public String getQuery() {
        return EpicPunishments.getInstance().getConfig().getBoolean("my_sql.enabled") ? mySqlQuery : sqLiteQuery;
    }
}
