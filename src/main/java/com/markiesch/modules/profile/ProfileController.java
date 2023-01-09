package com.markiesch.modules.profile;

import com.markiesch.database.DatabaseQuery;
import com.markiesch.database.SqlController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ProfileController extends SqlController<ProfileModel> {
    private static final DatabaseQuery CREATE = new DatabaseQuery(
            "INSERT OR REPLACE INTO Profile ([UUID], [ip], [name]) " +
                    "VALUES(?, ?, ?) " +
                    "ON CONFLICT([UUID]) " +
                    "DO UPDATE SET [ip] = ?, [name] = ?;",
            "INSERT INTO Profile (`UUID`, `ip`, `name`) " +
                    "VALUES(?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "`ip` = ?, `name` = ?;"
    );

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
        int affectedRows = executeUpdate(CREATE.getQuery(), new Object[]{uuidToBytes(uuid), ip, name, ip, name});
        return  affectedRows == 1;
    }

    public List<ProfileModel> getProfiles() {
        return executeRead("SELECT * FROM Profile;", null);
    }
}
