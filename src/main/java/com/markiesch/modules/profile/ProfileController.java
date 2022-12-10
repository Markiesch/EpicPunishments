package com.markiesch.modules.profile;

import com.markiesch.storage.Storage;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfileController {
    private final Storage storage;

    public ProfileController() {
        storage = Storage.getInstance();
    }

    /**
     * Creates a profile for the given player
     */
    public boolean createProfile(UUID uuid, String name, String ip) {
        try {
            Connection connection = storage.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT OR REPLACE INTO Profile (UUID, ip, name) " +
                            "VALUES(?, ?, ?) " +
                            "ON CONFLICT(UUID) " +
                            "DO UPDATE SET ip = ?; " +
                            "SELECT changes();"
            );
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, ip);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, ip);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to write to database");
        }
        return false;
    }

    public List<ProfileModel> getProfiles() {
        List<ProfileModel> models = new ArrayList<>();

        try {
            Connection connection = storage.getConnection();

            ResultSet result = connection.prepareStatement(ProfileQuery.SELECT_PROFILES).executeQuery();

            while (result.next()) {
                ProfileModel profileModel = new ProfileModel(
                        UUID.fromString(result.getString("UUID")),
                        result.getString("name"),
                        result.getString("ip"),
                        result.getString("textureURL")
                );
                models.add(profileModel);
            }
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to read from database");
        }

        return models;
    }

    public void updateTextureURL(UUID uuid, String url) {
        try {
            Connection connection = storage.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Profile " +
                            "SET [textureURL] = ?" +
                            "WHERE [uuid] = ?"
            );
            preparedStatement.setString(1, url);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to write to database");
        }
    }
}
