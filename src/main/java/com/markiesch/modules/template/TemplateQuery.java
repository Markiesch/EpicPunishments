package com.markiesch.modules.template;

public class TemplateQuery {
    public static String SELECT_ALL_TEMPLATES = "SELECT * FROM Template";
    public static String CREATE_TEMPLATE = "INSERT INTO Template (name, type, reason, duration)VALUES(?, ?, ?, ?)";
    public static String DELETE_TEMPLATE = "DELETE FROM Template WHERE [id] = ?;";
}
