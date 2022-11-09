package com.markiesch.modules.template;

public class TemplateQuery {
    public static final String SELECT_ALL_TEMPLATES = "SELECT * FROM Template";
    public static final String SELECT_SINGLE_TEMPLATE = "SELECT * FROM Template WHERE [id] = ?";
    public static final String CREATE_TEMPLATE = "INSERT INTO Template (name, type, reason, duration)VALUES(?, ?, ?, ?)";
    public static final String DELETE_TEMPLATE = "DELETE FROM Template WHERE [id] = ?;";
    public static final String UPDATE_TEMPLATE =
            "UPDATE Template " +
                "SET " +
                "name = ?," +
                "type = ?," +
                "reason = ?," +
                "duration = ? " +
                "WHERE [id] = ?;";
}
