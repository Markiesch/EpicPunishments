package com.markiesch.modules.profile;

import com.markiesch.storage.SqlController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ProfileController extends SqlController<ProfileModel> {
    @Override
    protected ProfileModel resultSetToModel(ResultSet resultSet) throws SQLException {
        return new ProfileModel(
                uuidFromBytes(resultSet.getBytes("uuid")),
                resultSet.getString("name"),
                resultSet.getString("ip")
        );
    }

    /**
     * Creates a profile for the given player
     */
    public boolean createProfile(UUID uuid, String name, String ip) {
        return executeUpdate("INSERT OR REPLACE INTO Profile (UUID, ip, name) " +
                        "VALUES(?, ?, ?) " +
                        "ON CONFLICT(UUID) " +
                        "DO UPDATE SET ip = ?, name = ?; " +
                        "SELECT changes();",
                new Object[]{uuidToBytes(uuid), ip, name, ip, name}) == 1;
    }

    public List<ProfileModel> getProfiles() {
        return executeRead("SELECT * FROM Profile;", null);
    }
}
