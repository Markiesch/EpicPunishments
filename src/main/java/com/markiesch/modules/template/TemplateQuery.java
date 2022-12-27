package com.markiesch.modules.template;

import org.intellij.lang.annotations.Language;

public class TemplateQuery {
    @Language("SQLite")
    public static final String SELECT_SINGLE_TEMPLATE = "SELECT * FROM Template WHERE [id] = ?";
    @Language("SQLite")
    public static final String CREATE_TEMPLATE = "INSERT INTO Template (name, type, reason, duration)VALUES(?, ?, ?, ?)";
    @Language("SQLite")
    public static final String DELETE_TEMPLATE = "DELETE FROM Template WHERE [id] = ?;";
    @Language("SQLite")
    public static final String UPDATE_TEMPLATE =
            "UPDATE Template " +
                "SET " +
                "name = ?," +
                "type = ?," +
                "reason = ?," +
                "duration = ? " +
                "WHERE [id] = ?;";
}
