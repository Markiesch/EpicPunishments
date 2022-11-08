package com.markiesch.modules.infraction;

public class InfractionQuery {
    public static final String SELECT_INFRACTION = "SELECT * FROM Infraction WHERE [id] = ?;";
    public static final String SELECT_INFRACTIONS = "SELECT * FROM Infraction WHERE victim = ?;";
    public static final String DELETE_INFRACTION = "DELETE FROM Infraction WHERE [id] = ?;";
}
