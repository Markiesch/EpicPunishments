package com.markiesch.modules.profile;

public class ProfileQuery {
    public static final String ADD_PROFILE =
            "INSERT OR REPLACE INTO Profile (UUID, ipHistory)" +
            "VALUES(?, ?) " +
            "ON CONFLICT(UUID) DO UPDATE SET " +
            "ipHistory = ipHistory || ';' || ?";
    public static final String SELECT_PROFILE = "SELECT * FROM Profile WHERE UUID = ?;";
    public static final String SELECT_PROFILES = "SELECT * FROM Profile;";
}
